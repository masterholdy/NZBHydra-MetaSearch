package com.github.masterholdy.backend.metasearch.nzb;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.util.URL;

import java.time.Instant;
import java.util.Date;

public class NZBRelease extends Release {
    public NZBRelease(String title, String category, Instant creationDate) {
        super(title,category,creationDate);
    }

    public NZBRelease(String title, String category, Instant creationDate, String href) {
        super(title, category, creationDate, href);
        this.href = href;
    }

    @Override
    public String getReleasePrefix() {
        return "[MetaSearch-NZB]";
    }

    @Override
    public String getHref() {
        String encodedURL = URL.encodeURL(super.getHref()).orElse("");
        return String.format("http://www.dereferer.org/?%s", encodedURL); //hydra kann bspw. & in urls nicht verabeiten
    }
}
