package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.order.BookingRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final ServiceRepository serviceRepository;

    public List<BookingDto> getAll() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    public BookingDto getById(long id) {
        return bookingRepository.findById(id)
                .map(BookingMapper::toDto)
                .orElse(null);
    }

    public BookingDto create(BookingNoIdDto dto) {
        Event event = eventRepository.getReferenceById(dto.getEventId());
        Service service = serviceRepository.getReferenceById(dto.getServiceId());
        Booking booking = BookingMapper.toEntity(dto, event, service);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto update(BookingNoIdDto dto, long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null)
            return null;

        Event event = eventRepository.getReferenceById(dto.getEventId());
        Service service = serviceRepository.getReferenceById(dto.getServiceId());

        booking.setEvent(event);
        booking.setService(service);
        booking.setPrice(dto.getPrice());
        booking.setDate(new Date(dto.getDate()));
        booking.setDuration(dto.getDuration());
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public boolean delete(long id) {
        if (!bookingRepository.existsById(id))
            return false;
        bookingRepository.deleteById(id);
        return true;
    }
}
