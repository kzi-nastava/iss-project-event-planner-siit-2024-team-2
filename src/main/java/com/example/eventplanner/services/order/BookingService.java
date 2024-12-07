package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.repositories.order.BookingRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

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
        Booking booking = BookingMapper.toEntity(dto, 0);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto update(BookingNoIdDto dto, long id) {
        if (!bookingRepository.existsById(id))
            return null;
        Booking booking = (Booking) BookingMapper.toEntity(dto, 0).withId(id);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public boolean delete(long id) {
        if (!bookingRepository.existsById(id))
            return false;
        bookingRepository.deleteById(id);
        return true;
    }
}
