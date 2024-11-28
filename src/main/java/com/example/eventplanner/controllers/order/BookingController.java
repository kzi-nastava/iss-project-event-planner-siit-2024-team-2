package com.example.eventplanner.controllers.order;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.services.order.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor()
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> getBookings() {
        Collection<BookingDto> result = bookingService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable("id") Long id) {
        BookingDto result = bookingService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingNoIdDto dto) {
        BookingDto result = bookingService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable("id") Long id, @RequestBody BookingNoIdDto dto) {
        BookingDto result = bookingService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<BookingDto> deleteBooking(@PathVariable("id") Long id) {
        boolean success = bookingService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
