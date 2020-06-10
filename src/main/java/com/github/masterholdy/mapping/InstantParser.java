package com.github.masterholdy.mapping;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class InstantParser {
    public static Instant fromString(String date, String datePattern, ZoneId zoneId, ChronoField chronoField) {
        DateTimeFormatter FMT = new DateTimeFormatterBuilder()
                .appendPattern(datePattern)
                .parseDefaulting(chronoField, 0)
                .toFormatter()
                .withZone(zoneId);
        java.time.Instant instant = FMT.parse(date, java.time.Instant::from);
        return instant;
    }

    public static Instant fromString(String date, String datePattern, ZoneId zoneId) {
        return fromString(date, datePattern, zoneId, ChronoField.NANO_OF_SECOND);
    }

    public static Instant fromString(String date, String datePattern) {
        return fromString(date, datePattern, ZoneId.of("Europe/LONDON"));
    }

    public static java.time.Instant fromDelta(int days, int hours, int minutes, int seconds){
            return Instant.now().minusMillis(days * 24 * 3600 * 1000l
                        + hours * 3600 * 1000L
                        + minutes * 60 * 1000L
                        + seconds * 1000L);
    }

    public static java.time.Instant fromDelta(int years, int months, int weeks, int days, int hours, int minutes, int seconds){
        return Instant.now().minusMillis(years * 12 * 4 * 7 * 24 * 3600 * 1000l
                + months * 4 * 7 * 24 * 3600 * 1000l
                + weeks * 7 * 24 * 3600 * 1000l
                + days * 24 * 3600 * 1000l
                + hours * 3600 * 1000L
                + minutes * 60 * 1000L
                + seconds * 1000L);
    }
}