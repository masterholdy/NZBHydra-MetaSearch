package com.github.masterholdy.backend.metasearch.nfo.predotcorruptnetdotorg;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.backend.metasearch.SimpleSearchWrapper;
import com.github.masterholdy.mapping.InstantParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
//TODO funkt nicht
public class PreDotCorruptNetDotOrg extends SimpleSearchWrapper {
    @Override
    protected Optional<String> buildQueryURL(String encodedQuery) {
        return Optional.of(String.format("%s%s", Consts.QUERY_URL, encodedQuery));
    }

    @Override
    protected Optional<Instant> parseDate(String date) {
        try{
            return Optional.of(
                    InstantParser.fromString(date, Consts.DATE_PATTERN)
            );
        }catch(Exception e){
            return Optional.of(Instant.now());
        }
    }

    @Override
    protected Optional<List<Release>> parseReleases(Document doc) {
        return parseReleasesSimple(doc, "#result_data > tbody > tr");
    }

    @Override
    protected Optional<Release> parseRelease(Element element) {
        try{
            String title = element.select("td:nth-child(2)").first().text();
            String group = element.select("td:nth-child(2) > span > font").first().text();
            String _cat = element.select("tr > td").first().text();
            String cat = String.format("%s(%s)", _cat, Consts.NAME);
            String dateS = element.select("td:nth-child(5)").first().text();

            title = String.format("%s%s", title, group);

            Instant date = parseDate(dateS).orElse(Instant.now());

            return Optional.of(new NFORelease(title, cat, date));
        }finally {
            return Optional.empty();
        }

    }

    @Override
    public String siteName() {
        return Consts.NAME;
    }
}
