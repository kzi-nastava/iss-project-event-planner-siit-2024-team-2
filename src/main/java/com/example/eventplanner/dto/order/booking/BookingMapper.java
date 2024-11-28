package com.example.eventplanner.dto.order.booking;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.model.order.Booking;

public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        if (booking == null)
            return null;
        return new BookingDto(
                booking.getId(),
                booking.getEvent().getId(),
                booking.getService().getId(),
                booking.getPrice(),
                booking.getDate(),
                booking.getDuration()
        );
    }

    public static BookingNoIdDto toDtoNoId(Booking booking) {
        if (booking == null)
            return null;
        return new BookingNoIdDto(
                booking.getEvent().getId(),
                booking.getService().getId(),
                booking.getPrice(),
                booking.getDate(),
                booking.getDuration()
        );
    }

    public static Booking toEntity(BookingDto dto) {
        if (dto == null)
            return null;
        Booking booking = new Booking(
                null,
                null,
                dto.getPrice(),
                dto.getDate(),
                dto.getDuration());
        booking.setId(dto.getId());
        booking.setActive(true);
        return booking;
    }

    public static Booking toEntity(BookingNoIdDto dto) {
        if (dto == null)
            return null;
        Booking booking = new Booking(
                null,
                null,
                dto.getPrice(),
                dto.getDate(),
                dto.getDuration());
        booking.setActive(true);
        return booking;
    }
}