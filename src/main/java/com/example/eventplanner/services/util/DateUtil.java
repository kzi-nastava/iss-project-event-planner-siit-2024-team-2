package com.example.eventplanner.services.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        ZoneId zoneId = ZoneId.of("Europe/Belgrade");
        ZonedDateTime eventDate = Instant.ofEpochMilli(date).atZone(zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", Locale.ENGLISH);

        return formatter.format(eventDate);
    }
}
