package com.github.masterholdy.backend.metasearch.nfo.predbdotme;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.backend.metasearch.SimpleSearchWrapper;
import com.github.masterholdy.mapping.InstantParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

/*
//TODO: cloudflare -> funkt nicht
 */
public class PreDbDotMeSearchWrapper extends SimpleSearchWrapper {
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
        return parseReleasesSimple(doc, "div.pl-body > div.post");
    }

    @Override
    protected Optional<Release> parseRelease(Element element) {
        String title = element.select("div.p-c.p-c-title > h2 > a.p-title").first().text();
        String _cat = element.select("div.p-c.p-c-cat > span").first().text();
        String cat = String.format("%s(%s)", _cat, Consts.NAME);
        String dateS = element.select("div.p-c.p-c-time > span-p-time > span.t-d").first().text();

        Instant date = parseDate(dateS).orElse(Instant.now());

        return Optional.of(new NFORelease(title, cat, date));
    }

    @Override
    public String siteName() {
        return Consts.NAME;
    }
}
