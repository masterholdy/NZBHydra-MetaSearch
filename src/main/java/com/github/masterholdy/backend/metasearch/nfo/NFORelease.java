package com.github.masterholdy.backend.metasearch.nfo;

import com.github.masterholdy.backend.metasearch.Release;

import java.time.Instant;
import java.util.Date;

public class NFORelease extends Release {
    public NFORelease(String title, String category, Instant creationDate) {
        super(title,category,creationDate);
    }

    public NFORelease(String title, String category, Instant creationDate, String href) {
        super(title, category, creationDate, href);
        this.href = href;
    }

    @Override
    public String getReleasePrefix() {
        return "[MetaSearch-NFO]";
    }
}
