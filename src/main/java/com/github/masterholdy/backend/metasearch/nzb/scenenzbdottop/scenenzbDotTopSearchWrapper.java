package com.github.masterholdy.backend.metasearch.nzb.scenenzbdottop;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.SimpleSearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.backend.metasearch.nzb.NZBRelease;
import com.github.masterholdy.mapping.InstantParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class scenenzbDotTopSearchWrapper extends SimpleSearchWrapper {

    Logger logger = LoggerFactory.getLogger(scenenzbDotTopSearchWrapper.class);

    @Override
    protected Optional<String> buildQueryURL(String encodedQuery) {
        return Optional.of(Consts.QUERY_URL(encodedQuery));
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
        return parseReleasesSimple(doc, "#releases > tr");
    }

    @Override
    protected Optional<Release> parseRelease(Element element) {
        Elements titleElements = element.select("td");

        if(titleElements.size() < 3) return Optional.empty();

        String _date = (titleElements.size() > 3) ? titleElements.get(4).text() : null;
        Instant date = parseDate(_date).orElse(Instant.now());

        String category = titleElements.get(1).text();


        Element titleElement = titleElements.get(2).select("a").first();
        if(titleElement == null) return Optional.empty();

        String title = titleElement.text();

        Element nzbElement = titleElements.get(3).select("a").first();
        if(nzbElement == null) return Optional.of(new NFORelease(title, category, date));
        String nzbHref = nzbElement.attr("href");

        Release release = new NZBRelease(title, category, date, nzbHref);

        return title.isEmpty() ?  Optional.empty() : Optional.of(release);
    }

    @Override
    public String siteName() {
        return Consts.NAME;
    }
}
