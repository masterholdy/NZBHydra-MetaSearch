package com.github.masterholdy.backend.metasearch;

import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import com.github.masterholdy.backend.util.URL;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class SimpleSearchWrapper implements SearchWrapper{
    boolean debug = true;
    boolean followRedirect = true;
    protected Logger logger = LoggerFactory.getLogger(SimpleSearchWrapper.class);

    String buffer = "";

    public SearchResult search(String query){
        String encodedQuery = URL.encodeURL(query).orElse(null);

        if(encodedQuery == null){
            return new SearchResult(-2, "couldn't encode Query.");
        }

        String QUERY_URL = this.buildQueryURL(encodedQuery).orElse(null);
        if(QUERY_URL == null){
            return new SearchResult(-3, "couldn't build Query-URL.");
        }

        logger.debug(String.format("%s: %s", this.siteName(), QUERY_URL));

        Connection con = this.getConnection(QUERY_URL);

        if(!validHttpConnection(con.response().statusCode())){
            return new SearchResult(con.response().statusCode(), String.format("[F-4] %s", con.response().statusMessage()));
        }

        Document doc = null;
        try {
            if(followRedirect){
                String s = con.execute().url().toExternalForm();
                logger.debug(String.format("%s: %s", "FollowRedirct[=true], Url: ", s));
                if(!s.equals(QUERY_URL)){
                    String referrer = QUERY_URL;
                    QUERY_URL = s;
                    con = this.getConnection(QUERY_URL, referrer);
                    if(!validHttpConnection(con.response().statusCode())){
                        return new SearchResult(con.response().statusCode(), String.format("[F-4] %s", con.response().statusMessage()));
                    }
                }
            }
            doc = con.get();
            logger.trace("Document: ");
            logger.trace(doc.toString());

        } catch (IOException e) {
            return new SearchResult(-5, "couldn't parse HTML.");
        }

        List<Release> releases = null;
        try{
            releases = this.parseReleases(doc).orElse(null);
            logger.debug("Releases (Parsed(Doc)): ");
            logger.debug(releases.toString());
        }catch(Exception e){
            logger.error(String.format("%s: EXCEPTION - %s", this.siteName(), e.toString()));
        }


        if(releases==null){
            return new SearchResult(-6, "couldn't parse Releases.");
        }

        return new SearchResult(releases);
    }

    public boolean validHttpConnection(int statusCode){
        return statusCode == 0 || statusCode == 200 || statusCode == 201;
    }

    protected Connection getConnection(String QUERY_URL){
        return getConnection(QUERY_URL, "http://www.google.com");
    }

    protected Connection getConnection(String QUERY_URL, String referrer){
        return Jsoup.connect(QUERY_URL)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate")
                .header("accept-language", "en-US,en;q=0.8")
                .header("cache-control", "max-age=0")
                .header("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                .header("upgrade-insecure-requests", "1")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .method(Connection.Method.GET)
                .referrer(referrer)
                .timeout(120000)
                .followRedirects(true);
    }

    protected Optional<List<Release>> parseReleasesSimple(Document doc, String selector) {
        Elements elements = doc.select(selector);

        List<Release> releases = new ArrayList<>();

        for(Element e : elements){
            Release rls = this.parseRelease(e).orElse(null);
            if(rls!=null) releases.add(rls);
        }

        return Optional.of(releases);
    }

    protected Optional<String> encodeQuery(String query) {
        return URL.encodeURL(query);
    }

        protected abstract Optional<String> buildQueryURL(String encodedQuery);
    protected abstract Optional<Instant> parseDate(String date);
    protected abstract Optional<List<Release>> parseReleases(Document doc);
    protected abstract Optional<Release> parseRelease(Element element);
}
