package com.github.masterholdy.backend.metasearch;

import com.github.masterholdy.backend.metasearch.nfo.NFORelease;

import java.util.List;

public class SearchResult {
    String errorMessage;
    int statusCode = -1;
    List<Release> releases;

    public SearchResult(int statusCode, String statusMessage) {
        this.errorMessage = String.format("[F%d] - [%s]", statusCode, statusMessage);
        this.statusCode = statusCode;
    }

    public SearchResult(List<Release> releases) {
        this.releases = releases;
    }

    public boolean isSuccessful(){
        return statusCode < 0 || statusCode != 200;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<Release> getReleases() {
        return releases;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "errorMessage='" + errorMessage + '\'' +
                ", statusCode=" + statusCode +
                ", releases=" + releases +
                '}';
    }
}
