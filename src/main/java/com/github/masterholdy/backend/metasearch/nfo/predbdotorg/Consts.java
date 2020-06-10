package com.github.masterholdy.backend.metasearch.nfo.predbdotorg;

public class Consts {
    public static final String NAME = "predb.org";
    public static final String URL = "https://predb.org";
    public static final String QUERY_URL(String encodedQuery){
        return String.format("%s/search/%s/all", URL, encodedQuery);
    }
}