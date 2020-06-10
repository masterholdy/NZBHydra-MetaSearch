package com.github.masterholdy.backend.metasearch;

import com.github.masterholdy.backend.metasearch.nfo.NFORelease;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class SearchJob extends Thread {
    Logger logger = LoggerFactory.getLogger(SearchJob.class);

    SearchWrapper searchWrapper;
    String query;
    Optional<List<Release>> result;

    public SearchJob(SearchWrapper searchWrapper, String query){
        this.searchWrapper=searchWrapper;
        this.query = query;
    }

    public void run() {
        SearchResult sResult = null;
        try{
            logger.debug(String.format("Searching %s: %s", searchWrapper.siteName(), query));
            sResult = searchWrapper.search(query);
        }catch(Exception e){
            logger.error(searchWrapper.siteName());
            logger.error(e.toString());
        }

        if(sResult!=null && sResult.getReleases()!=null){
            logger.debug(String.format("Searching %s: %d Results", searchWrapper.siteName(), sResult.getReleases().size()));
            this.result = Optional.of(sResult.releases);

        }else{
            this.result = Optional.empty();
        }
    }

    public Optional<List<Release>> getResult() {
        return result;
    }

}
