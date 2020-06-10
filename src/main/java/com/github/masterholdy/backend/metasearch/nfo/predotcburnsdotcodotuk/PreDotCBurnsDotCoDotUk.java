package com.github.masterholdy.backend.metasearch.nfo.predotcburnsdotcodotuk;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.backend.metasearch.SimpleSearchWrapper;
import com.github.masterholdy.mapping.InstantParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreDotCBurnsDotCoDotUk extends SimpleSearchWrapper {
    Logger logger = LoggerFactory.getLogger(PreDotCBurnsDotCoDotUk.class);

    @Override
    protected Optional<String> buildQueryURL(String encodedQuery) {
        return Optional.of(String.format("%s%s", Consts.QUERY_URL, encodedQuery));
    }

    @Override
    protected Optional<Instant> parseDate(String date) {
        Pattern pattern = Pattern.compile("(\\d{1,3}(Y|M|W|D|h|m|s))");
        Matcher matcher = pattern.matcher(date);

        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        while(matcher.find()){
            String t = matcher.group();
            try{
                if(t.endsWith("Y")){
                    t = t.replace("Y", "");
                    years = Integer.parseInt(t);
                }
                if(t.endsWith("M")){
                    t = t.replace("M", "");
                    months = Integer.parseInt(t);
                }
                if(t.endsWith("W")){
                    t = t.replace("W", "");
                    weeks = Integer.parseInt(t);
                }
                if(t.endsWith("D")){
                    t = t.replace("D", "");
                    days = Integer.parseInt(t);
                }
                if(t.endsWith("h")){
                    t = t.replace("h", "");
                    hours = Integer.parseInt(t);
                }
                if(t.endsWith("m")){
                    t = t.replace("m", "");
                    minutes = Integer.parseInt(t);
                }
                if(t.endsWith("s")){
                    t = t.replace("s", "");
                    seconds = Integer.parseInt(t);
                }
            }catch (Exception e){}

        }

        return Optional.of(InstantParser.fromDelta(years, months, weeks, days, hours, minutes, seconds));
    }

    @Override
    protected Optional<List<Release>> parseReleases(Document doc) {
        return parseReleasesSimple(doc, "tbody > tr:nth-child(n+4)");
    }

    @Override
    protected Optional<Release> parseRelease(Element element) {
        String title = element.select("td:nth-child(1) > a").first().text();
        String URLPATH = element.select("td:nth-child(1) > a").attr("href");
        String HREF = Consts.URL + "/" + URLPATH;
        String _cat = element.select("td:nth-child(2) > a").first().text();
        String cat = String.format("%s(%s)", _cat, Consts.NAME);
        String dateS = element.select("td:nth-child(3)").first().text();

        Instant date = parseDate(dateS).orElse(Instant.now());

        return Optional.of(new NFORelease(title, cat, date, HREF));
    }

    @Override
    public String siteName() {
        return Consts.NAME;
    }
}
