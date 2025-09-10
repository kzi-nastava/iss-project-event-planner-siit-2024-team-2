package com.example.eventplanner.controllers.order;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.order.booking.PendingBookingDto;
import com.example.eventplanner.dto.user.userreport.UserReportDto;
import com.example.eventplanner.services.order.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor()
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> getAllBookings() {
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
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingNoIdDto dto) {
        BookingDto result = bookingService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable("id") Long id, @Valid @RequestBody BookingNoIdDto dto) {
        BookingDto result = bookingService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<BookingDto> deleteBooking(@PathVariable("id") Long id) {
        return bookingService.delete(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<BookingDto> acceptBooking(@PathVariable("id") Long id) {
        BookingDto result = bookingService.accept(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<Page<PendingBookingDto>> getMyBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size != null ? size : 10)
                .withSort(Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PendingBookingDto> result = bookingService.getMyBookings(pageable);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
