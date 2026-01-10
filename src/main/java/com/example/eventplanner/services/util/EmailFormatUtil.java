package com.example.eventplanner.services.util;

import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EmailFormatUtil {
    private static final long HOUR_MS = 60 * 60 * 1000;

    public static String formatInviteEmail(EventSummaryDto eventSummary, String inviteLink, boolean requiresRegistration) {

        ClassPathResource resource = new ClassPathResource("templates/invitation_email.html");
        String html;
        try (InputStream inputStream = resource.getInputStream()) {
            html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
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
                .replace("{{unregisteredHintDisplay}}", requiresRegistration ? "block" : "none");
        return html;
    }

    public static String formatEOBookingRequestEmail(EventSummaryDto eventSummary, Booking booking) {
        ClassPathResource resource = new ClassPathResource("templates/eo_booking_request_email.html");
        String html;
        try (InputStream inputStream = resource.getInputStream()) {
            html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }

        String formattedDate = DateUtil.formatDate(eventSummary.getDate());
        String formattedTimePeriod = DateUtil.formatTimePeriod(booking.getDate().toEpochMilli(),
                booking.getDate().toEpochMilli() + (long)(HOUR_MS * booking.getDuration())) + " UTC";
        String mapUrl = String.format("https://www.openstreetmap.org/?mlat=%s&mlon=%s&zoom=16", eventSummary.getLatitude(), eventSummary.getLongitude());

        Service service = booking.getService();
        String sppName = "Deleted user", sppEmail = "";
        if (service.getServiceProductProvider() != null) {
            sppName = service.getServiceProductProvider().getFirstName() + " " + service.getServiceProductProvider().getLastName();
            sppEmail = service.getServiceProductProvider().getEmail();
        }

        html = html.replace("{{eventName}}", eventSummary.getName())
                .replace("{{serviceName}}", service.getName())
                .replace("{{bookingDate}}", formattedDate)
                .replace("{{bookingTimePeriod}}", formattedTimePeriod)
                .replace("{{price}}", booking.getPrice() + " €")
                .replace("{{sppName}}", sppName)
                .replace("{{sppEmail}}", sppEmail)
                .replace("{{mapUrl}}", mapUrl)
                .replace("{{eventDescription}}", eventSummary.getDescription());
        return html;
    }

    public static String formatBookingConfirmationEmail(EventSummaryDto eventSummary, Booking booking) {
        ClassPathResource resource = new ClassPathResource("templates/booking_confirmation_email.html");
        String html;
        try (InputStream inputStream = resource.getInputStream()) {
            html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }

        String formattedDate = DateUtil.formatDate(eventSummary.getDate());
        String formattedTimePeriod = DateUtil.formatTimePeriod(booking.getDate().toEpochMilli(),
                booking.getDate().toEpochMilli() + (long)(HOUR_MS * booking.getDuration())) + " UTC";
        String mapUrl = String.format("https://www.openstreetmap.org/?mlat=%s&mlon=%s&zoom=16", eventSummary.getLatitude(), eventSummary.getLongitude());

        Service service = booking.getService();
        String sppName = "Deleted user", sppEmail = "";
        if (service.getServiceProductProvider() != null) {
            sppName = service.getServiceProductProvider().getFirstName() + " " + service.getServiceProductProvider().getLastName();
            sppEmail = service.getServiceProductProvider().getEmail();
        }

        String eoName = "Deleted user", eoEmail = "";
        if (eventSummary.getCreatorEmail() != null) {
            eoName = eventSummary.getCreatorName();
            eoEmail = eventSummary.getCreatorEmail();
        }

        html = html.replace("{{eventName}}", eventSummary.getName())
                .replace("{{serviceName}}", service.getName())
                .replace("{{bookingDate}}", formattedDate)
                .replace("{{bookingTimePeriod}}", formattedTimePeriod)
                .replace("{{price}}", booking.getPrice() + " €")
                .replace("{{sppName}}", sppName)
                .replace("{{sppEmail}}", sppEmail)
                .replace("{{organizerName}}", eoName)
                .replace("{{organizerEmail}}", eoEmail)
                .replace("{{mapUrl}}", mapUrl)
                .replace("{{eventDescription}}", eventSummary.getDescription());
        return html;
    }
}
