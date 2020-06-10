package com.github.masterholdy.backend.metasearch.nfo.predbdotorg;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.backend.metasearch.SimpleSearchWrapper;
import com.github.masterholdy.mapping.InstantParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.Instant;
import java.util.*;

public class PreDbDotOrgSearchWrapper extends SimpleSearchWrapper {
    @Override
    protected Optional<String> buildQueryURL(String encodedQuery) {
        return Optional.of(Consts.QUERY_URL(encodedQuery));
    }

    @Override
    protected Optional<Instant> parseDate(String date) {
        String[] split = date.split(" ");
        String _d = "0";
        String _h = "0";
        String _m = "0";
        String _s = "0";
        //162d 6h 53m 35s -> case 4 bis d || case 1 => nur s vorhanden
        switch (split.length){
            case 4:
                if(split[0] != null && split[0].length() > 0) _d = split[0].substring(0, split[0].length() - 1);
                if(split[1] != null && split[1].length() > 0) _h = split[1].substring(0, split[1].length() - 1);
                if(split[2] != null && split[2].length() > 0) _m = split[2].substring(0, split[2].length() - 1);
                if(split[3] != null && split[3].length() > 0) _s = split[3].substring(0, split[3].length() - 1);
                break;
            case 3:
                if(split[0] != null && split[0].length() > 0) _h = split[0].substring(0, split[0].length() - 1);
                if(split[1] != null && split[1].length() > 0) _m = split[1].substring(0, split[1].length() - 1);
                if(split[2] != null && split[2].length() > 0) _s = split[2].substring(0, split[2].length() - 1);
                break;
            case 2:
                if(split[0] != null && split[0].length() > 0) _m = split[0].substring(0, split[0].length() - 1);
                if(split[1] != null && split[1].length() > 0) _s = split[1].substring(0, split[1].length() - 1);
                break;
            case 1:
                if(split[0] != null && split[0].length() > 0) _s = split[0].substring(0, split[0].length() - 1);
                break;
            default:
                break;
        }

        Integer d = Integer.valueOf(_d);
        Integer h = Integer.valueOf(_h);
        Integer m = Integer.valueOf(_m);
        Integer s = Integer.valueOf(_s);

        if(d==null) d = 0;
        if(h==null) h = 0;
        if(m==null) m = 0;
        if(s==null) s = 0;

        return Optional.of(InstantParser.fromDelta(d, h, m, s));
    }

    @Override
    protected Optional<List<Release>> parseReleases(Document doc) {
        return parseReleasesSimple(doc, "tr.post");
    }

    @Override
    protected Optional<Release> parseRelease(Element element) {
        String title = element.select("td.rls > a > h1").first().text();
        String hrefPath = element.select("td.rls > a").first().attr("href");
        String href = Consts.URL + hrefPath;
        String _cat = element.select("td:nth-child(2) > a > font").first().text();
        String cat = String.format("%s(%s)", _cat, Consts.NAME);
        String dateS = element.select("th").first().text();

        Instant date = parseDate(dateS).orElse(Instant.now());

        return Optional.of(new NFORelease(title, cat, date, href));
    }

    @Override
    public String siteName() {
        return Consts.NAME;
    }
}
