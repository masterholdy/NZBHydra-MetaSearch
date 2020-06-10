package com.github.masterholdy.backend.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class URL {
    public static Optional<String> encodeURL(String url){
        try {
            String encodedURL = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
            return Optional.of(encodedURL);

        } catch (UnsupportedEncodingException e) {
            return Optional.empty();
        }
    }
}
