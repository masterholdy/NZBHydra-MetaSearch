package com.github.masterholdy.backend.metasearch.nzb.scenenzbdottop;

public class Consts {
    public static final String NAME = "scenenzb.top";
    public static final String URL = "https://scenenzb.top";
    public static final String QUERY_URL(String encodedQuery){
        return String.format("%s/?searchstring=%s&category=All", URL, encodedQuery);
    }

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
}
