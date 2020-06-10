package com.github.masterholdy.backend.metasearch;

import lombok.Data;
import org.nzbhydra.mapping.newznab.xml.NewznabXmlEnclosure;
import org.nzbhydra.mapping.newznab.xml.NewznabXmlGuid;
import org.nzbhydra.mapping.newznab.xml.NewznabXmlItem;
import org.nzbhydra.mapping.newznab.xml.NewznabXmlRoot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
//TODO date = date - ~1d
@Data
public abstract class Release extends NewznabXmlItem{
    protected String href;
    public Release(String title, String category, Instant pubDate) {
        this.setTitle(title);
        this.setCategory(category);
        this.setPubDate(pubDate);

        setFallBackValues();
    }

    public Release(String title, String category, Instant pubDate, String href) {
        this.setTitle(title);
        this.setCategory(category);
        this.setPubDate(pubDate);
        this.setLink(href);
        this.href = href;
        setFallBackValues();
    }

    public String getHref(){
        return (href == null || href.isEmpty())? String.format("https://www.google.com/search?q=%s", this.getTitle()) : this.href;
    }

    public String getReleasePrefix() {
        return "[MetaSearch]";
    }

    public void setFallBackValues(){
        if(this.getEnclosure()==null) this.setEnclosure(getMockEnclosure());
        if(this.getRssGuid()==null) this.setRssGuid(getMockGuid());
        if(this.getComments()==null) this.setComments(this.getHref());
        if(this.getDescription()==null) this.setDescription(getReleasePrefix());
    }

    public NewznabXmlEnclosure getMockEnclosure(){
        return new NewznabXmlEnclosure(this.getHref(), 99999999L, "application/x-nzb");
    }

    public NewznabXmlGuid getMockGuid(){
        return new NewznabXmlGuid(this.getHref(), true);
    }
}
