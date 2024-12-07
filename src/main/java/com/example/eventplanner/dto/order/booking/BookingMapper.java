package com.example.eventplanner.dto.order.booking;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.serviceproduct.service.ServiceMapper;
import com.example.eventplanner.model.order.Booking;

import java.util.Date;

public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        if (booking == null)
            return null;
        return new BookingDto(
                booking.getId(),
                EventMapper.toDto(booking.getEvent()),
                ServiceMapper.toDto(booking.getService()),
                booking.getPrice(),
                booking.getDate().getTime(),
                booking.getDuration()
        );
    }

    public static BookingNoIdDto toDtoNoId(Booking booking) {
        if (booking == null)
            return null;
        return new BookingNoIdDto(
                EventMapper.toDto(booking.getEvent()),
                ServiceMapper.toDto(booking.getService()),
                booking.getPrice(),
                booking.getDate().getTime(),
                booking.getDuration()
        );
    }

    public static Booking toEntity(BookingDto dto, int depth) {
        if (dto == null || depth > 1)
            return null;
        return (Booking) new Booking(
                EventMapper.toEntity(dto.getEvent(), depth + 1),
                ServiceMapper.toEntity(dto.getService(), depth + 1),
                dto.getPrice(),
                new Date(dto.getDate()),
                dto.getDuration()).withId(dto.getId());
    }

    public static Booking toEntity(BookingNoIdDto dto, int depth) {
        if (dto == null || depth > 1)
            return null;
        return new Booking(
                EventMapper.toEntity(dto.getEvent(), depth + 1),
                ServiceMapper.toEntity(dto.getService(), depth + 1),
                dto.getPrice(),
                new Date(dto.getDate()),
                dto.getDuration());
    }
}