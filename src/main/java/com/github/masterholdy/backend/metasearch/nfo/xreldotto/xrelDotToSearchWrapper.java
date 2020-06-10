package com.github.masterholdy.backend.metasearch.nfo.xreldotto;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.backend.metasearch.SimpleSearchWrapper;
import com.github.masterholdy.mapping.InstantParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class xrelDotToSearchWrapper extends SimpleSearchWrapper {
    Logger logger = LoggerFactory.getLogger(xrelDotToSearchWrapper.class);

    @Override
    protected Optional<String> buildQueryURL(String encodedQuery) {
        return Optional.of(String.format("%s%s", Consts.QUERY_URL, encodedQuery));
    }
    @Override
    protected Optional<Instant> parseDate(String date) {
        try{
            return Optional.of(
                    InstantParser.fromString(date, Consts.DATE_PATTERN,  ZoneId.of("Europe/Berlin"))
            );
        }catch(Exception e){
            return Optional.of(Instant.now());
        }
    }

    @Override
    protected Optional<List<Release>> parseReleases(Document doc) {
        return parseReleasesSimple(doc, "#search_result_frame > div.release_item");
    }

    @Override
    protected Optional<Release> parseRelease(Element element) {
        Elements titleElements = element.select("div.release_title > a");
        if(titleElements.size() == 0){
            titleElements = element.select("div.release_title_p2p > a");
        }

        if(titleElements.size() == 0){
            return Optional.empty();
        }

        String title = titleElements.get(1).attr("title");
        String URLPATH = titleElements.get(1).attr("href");
        String href = Consts.URL + URLPATH;
        String _cat = element.select("div.release_cat > a > span").first().text();
        String cat = String.format("%s(%s)", _cat, Consts.NAME);

        String dateS = element.select("div.release_date").first().text().replace(" Uhr", ""); //" dd.mm.yy hh:mm Uhr"

        Instant date = parseDate(dateS).orElse(Instant.now());

        Release relase = new NFORelease(title, cat, date, href);

        return title.isEmpty() ?  Optional.empty() : Optional.of(relase);
    }

    @Override
    public String siteName() {
        return Consts.NAME;
    }
}
