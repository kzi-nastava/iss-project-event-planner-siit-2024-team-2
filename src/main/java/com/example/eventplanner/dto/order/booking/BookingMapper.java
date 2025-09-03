package com.example.eventplanner.dto.order.booking;

import com.example.eventplanner.dto.serviceproduct.service.ServiceMapper;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.BookingStatus;

import java.time.Instant;

public class BookingMapper {
    private BookingMapper() {}

    public static BookingDto toDto(Booking booking) {
        if (booking == null)
            return null;

        return new BookingDto(
                booking.getId(),
                ServiceMapper.toDto(booking.getService()),
                booking.getPrice(),
                booking.getDate().toEpochMilli(),
                booking.getDuration(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }

    public static Booking toEntity(BookingNoIdDto dto, Service service) {
        if (dto == null)
            return null;

        return new Booking(
                service,
                dto.getPrice(),
                dto.getDate(),
                dto.getDuration(),
                service.isAutomaticReserved() ? BookingStatus.ACCEPTED : BookingStatus.PENDING,
                Instant.now(),
                false);
    }

    public static PendingBookingDto toPendingDto(Booking booking, EventOrganizer eventOrganizer) {
        if (booking == null)
            return null;

        return new PendingBookingDto(
                booking.getId(),
                ServiceMapper.toDto(booking.getService()),
                booking.getPrice(),
                booking.getDate().toEpochMilli(),
                booking.getDuration(),
                booking.getCreatedAt(),
                eventOrganizer != null ? eventOrganizer.getFirstName() + " " + eventOrganizer.getLastName() : null,
                eventOrganizer != null ? eventOrganizer.getEmail() : null
        );
    }
}