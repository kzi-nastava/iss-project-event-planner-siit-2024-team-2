package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
@Getter
@Setter
public class BookingService {
    Map<Long, Booking> bookings = new HashMap<>();
    private long idCounter = 0;

    public List<BookingDto> getAll() {
        return bookings.values()
                .stream()
                .filter(Entity::isActive)
                .map(BookingMapper::toDto)
                .toList();
    }

    public BookingDto getById(long id) {
        if (!bookings.containsKey(id) || !bookings.get(id).isActive())
            return null;
        return BookingMapper.toDto(bookings.get(id));
    }

    public BookingDto create(BookingNoIdDto dto) {
        Booking booking = BookingMapper.toEntity(dto);
        booking.setId(idCounter++);
        booking.setActive(true);

        // link event
        Event testEvent = new Event();
        testEvent.setId(dto.getEventId());
        booking.setEvent(testEvent);
        // link service
        Service testService = new Service();
        testService.setId(dto.getServiceId());
        booking.setService(testService);

        bookings.put(booking.getId(), booking);
        return BookingMapper.toDto(booking);
    }

    public BookingDto update(BookingNoIdDto dto, long id) {
        if (this.getById(id) == null)
            return null;
        Booking booking = BookingMapper.toEntity(dto);

        // link event
        Event testEvent = new Event();
        testEvent.setId(dto.getEventId());
        booking.setEvent(testEvent);
        // link service
        Service testService = new Service();
        testService.setId(dto.getServiceId());
        booking.setService(testService);

        bookings.put(id, BookingMapper.toEntity(dto));
        return BookingMapper.toDto(booking);
    }

    public boolean delete(long id) {
        if (this.getById(id) == null)
            return false;
        bookings.get(id).setActive(false);
        return true;
    }
}
