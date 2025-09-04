package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.repositories.event.EventRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class EventReportService {

    @Autowired
    private EventRepository eventRepository;

    public byte[] generateEventPdf(long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Event: " + event.getName()));
            document.add(new Paragraph("Description: " + event.getDescription()));
            document.add(new Paragraph("Type: " + event.getType().getName()));
            document.add(new Paragraph("Organizer: " + event.getEventOrganizer().getFirstName()));
            document.add(new Paragraph("Max Attendances: " + event.getMaxAttendances()));
            document.add(new Paragraph("Open: " + (event.isOpen() ? "Yes" : "No")));
            document.add(new Paragraph("Date: " + event.getDate().toString().split(" ")[0]));
            String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query="
                    + event.getLatitude() + "," + event.getLongitude();

            Anchor eventLocationLink = new Anchor(
                    "Click the link"
            );
            eventLocationLink.setReference(googleMapsUrl);

            Paragraph locationParagraph = new Paragraph("Location: ");
            locationParagraph.add(eventLocationLink);
            document.add(locationParagraph);

            document.add(new Paragraph("\nActivities:"));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell(new PdfPCell(new Phrase("Name")));
            table.addCell(new PdfPCell(new Phrase("Time")));
            table.addCell(new PdfPCell(new Phrase("Description")));
            table.addCell(new PdfPCell(new Phrase("Location")));

            for (Activity a : event.getActivities()) {
                table.addCell(a.getName());
                table.addCell(formatMillisToTime(a.getActivityStart()) + " - " + formatMillisToTime(a.getActivityEnd()));
                table.addCell(a.getDescription());
                //table.addCell(a.getLocation());
                Anchor locationLink = new Anchor(a.getLocation());
                locationLink.setReference("https://www.google.com/maps/search/?api=1&query=" +
                        URLEncoder.encode(a.getLocation(), StandardCharsets.UTF_8));
                table.addCell(locationLink);
            }

            document.add(table);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public static String formatMillisToTime(long millis) {
        long totalMinutes = millis / (1000 * 60);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

}
