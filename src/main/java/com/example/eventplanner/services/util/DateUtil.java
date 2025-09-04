package com.example.eventplanner.services.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private DateUtil() {}

    public static Date convertLocalDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static String formatDate(long date) {
        ZonedDateTime eventDate = Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", Locale.ENGLISH);

        return formatter.format(eventDate);
    }

    public static String formatTime(long date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
        ZonedDateTime time = Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC);
        return formatter.format(time);
    }
    public static String formatTimePeriod(long startDate, long endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
        ZonedDateTime start = Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC);
        ZonedDateTime end = Instant.ofEpochMilli(endDate).atZone(ZoneOffset.UTC);
        return formatter.format(start) + " - " + formatter.format(end);
    }
}
