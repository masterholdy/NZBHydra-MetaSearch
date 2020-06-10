package com.github.masterholdy.backend.metasearch;

public interface SearchWrapper {
    SearchResult search(String query);
    String siteName();
}
