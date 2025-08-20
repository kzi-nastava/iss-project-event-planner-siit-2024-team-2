package com.example.eventplanner.services.util;

import com.example.eventplanner.dto.event.event.EventSummaryDto;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EmailFormatUtil {
    public static String formatInviteEmail(EventSummaryDto eventSummary, String inviteLink, boolean isRegistered) {

        ClassPathResource resource = new ClassPathResource("templates/invitation_email.html");
        String html;
        try {
            html = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return inviteLink;
        }

        String formattedDate = DateUtil.formatDate(eventSummary.getDate());
        String mapUrl = String.format("https://www.openstreetmap.org/?mlat=%s&mlon=%s&zoom=16", eventSummary.getLatitude(), eventSummary.getLongitude());

        html = html.replace("{{eventName}}", eventSummary.getName())
                .replace("{{creatorName}}", eventSummary.getCreatorName())
                .replace("{{creatorEmail}}", eventSummary.getCreatorEmail())
                .replace("{{eventDate}}", formattedDate)
                .replace("{{eventType}}", eventSummary.getType().getName())
                .replace("{{mapUrl}}", mapUrl)
                .replace("{{eventDescription}}", eventSummary.getDescription())
                .replace("{{inviteLink}}", inviteLink)
                .replace("{{unregisteredHintDisplay}}", isRegistered ? "none" : "block");
        return html;
    }
}
