package com.github.masterholdy.test;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.SearchResult;
import com.github.masterholdy.backend.metasearch.SearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.predbdotme.PreDbDotMeSearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.predbdotorg.PreDbDotOrgSearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.predotcburnsdotcodotuk.PreDotCBurnsDotCoDotUk;
import com.github.masterholdy.backend.metasearch.nfo.predotcorruptnetdotorg.PreDotCorruptNetDotOrg;
import com.github.masterholdy.backend.metasearch.nfo.srrDBDotCom.srrDBDotComSearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.xreldotto.Consts;
import com.github.masterholdy.backend.metasearch.nfo.xreldotto.xrelDotToSearchWrapper;
import com.github.masterholdy.backend.metasearch.nzb.scenenzbdottop.scenenzbDotTopSearchWrapper;
import com.github.masterholdy.mapping.InstantParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class TestMain {
    static SearchWrapper[] nfoSearchWrappers = new SearchWrapper[]{
            new xrelDotToSearchWrapper(),
            new PreDbDotMeSearchWrapper(), new PreDbDotOrgSearchWrapper(),
            new PreDotCBurnsDotCoDotUk(), new PreDotCorruptNetDotOrg(),
            new srrDBDotComSearchWrapper(),
            new scenenzbDotTopSearchWrapper()
    };
    public static void main(String[] args) throws ParseException {
        /*String d = "16.03.14 12:47";
        String patter = "dd.MM.yyyy HH:mm";
        Instant t = InstantParser.fromString(d, Consts.DATE_PATTERN,  ZoneId.of("Europe/Berlin"));
        System.out.println(t);*/
        SearchWrapper sw = new xrelDotToSearchWrapper();
        SearchResult sr = sw.search("joko gegen klaas");
        System.out.println(sr);
        sr.getReleases().forEach(System.out::println);

    }
}
