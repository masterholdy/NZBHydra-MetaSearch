package com.github.masterholdy.backend.metasearch.nfo.srrDBDotCom;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.SimpleSearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.mapping.InstantParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;

public class srrDBDotComSearchWrapper extends SimpleSearchWrapper {
    Logger logger = LoggerFactory.getLogger(srrDBDotComSearchWrapper.class);

    @Override
    protected Optional<String> encodeQuery(String query) {
        query = query.replace(" ", "/");
        return Optional.of(query);
    }

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
        return parseReleasesSimple(doc, "div.box-content > div > table > tbody > tr");

    }

    @Override
    protected Optional<Release> parseRelease(Element element) {
        String title = element.select("td:nth-child(2) > a").first().text();
        String cat = String.format("n/a(%s)", Consts.NAME);
        String dateS = element.select("td:nth-child(3) > span").first().text();
        String urlPath = element.select("td > a").attr("href");
        String href = Consts.URL + "/" + urlPath;

        Instant date = parseDate(dateS).orElse(Instant.now());

        return Optional.of(new NFORelease(title, cat, date, href));
    }

    @Override
    public String siteName() {
        return Consts.NAME;
    }
}
