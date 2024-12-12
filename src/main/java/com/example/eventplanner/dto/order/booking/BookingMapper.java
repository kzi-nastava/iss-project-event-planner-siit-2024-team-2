package com.example.eventplanner.dto.order.booking;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.serviceproduct.service.ServiceMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;

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

    public static Booking toEntity(BookingNoIdDto dto, Event event, Service service) {
        if (dto == null)
            return null;
        return new Booking(
                event,
                service,
                dto.getPrice(),
                new Date(dto.getDate()),
                dto.getDuration());
    }
}