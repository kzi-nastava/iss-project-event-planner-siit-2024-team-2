package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.util.DateRangeDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.order.BookingRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import java.util.*;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final ServiceRepository serviceRepository;

    static final long HOUR_MS = 60 * 60 * 1000;
    static final long DAY_MS = 24 * HOUR_MS;

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
        Service service = serviceRepository.getReferenceById(dto.getServiceId());
        Booking booking = BookingMapper.toEntity(dto, service);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto update(BookingNoIdDto dto, long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null)
            return null;

        Service service = serviceRepository.getReferenceById(dto.getServiceId());

        booking.setService(service);
        booking.setPrice(dto.getPrice());
        booking.setDuration(dto.getDuration());
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public boolean delete(long id) {
        if (!bookingRepository.existsById(id))
            return false;
        bookingRepository.deleteById(id);
        return true;
    }

    public Collection<BookingDto> getBookingsByProvider(Long providerId) {
        List<Booking> bookings = bookingRepository.findByProviderId(providerId);
        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    public boolean isAvailable(Long serviceId, Long eventId, DateRangeDto dateRange) {
        List<DateRangeDto> availableDates = getAvailableDates(serviceId, eventId);
        return availableDates.stream()
                .anyMatch(d -> d.getStart() <= dateRange.getStart() && d.getEnd() >= dateRange.getEnd());
    }

    public List<DateRangeDto> getAvailableDates(Long serviceId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return new ArrayList<>();
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null)
            return new ArrayList<>();
        Long startDate = new Date().getTime() + service.getReservationDaysDeadline() * DAY_MS;
        Long endDate = event.getDate().getTime() + DAY_MS;
        List<DateRangeDto> bookedDates = getBookedDates(service, startDate, endDate);
        return convertToAvailable(startDate, endDate, bookedDates);
    }

    private List<DateRangeDto> getBookedDates(Service service, Long startDate, Long endDate) {
        List<DateRangeDto> bookedDates = bookingRepository.findByServiceId(service.getId())
                        .stream()
                        .map(b -> new DateRangeDto(
                                b.getDate().getTime(),
                                b.getDate().getTime() + (long)(HOUR_MS * b.getDuration())))
                        .sorted(Comparator.comparing(DateRangeDto::getStart))
                        .filter(b -> b.getEnd() >= startDate && b.getStart() <= endDate)
                        .toList();
        float duration = service.getDuration() > 0
                ? service.getDuration()
                : service.getMinEngagementDuration();
        float durationMs = duration * HOUR_MS;
        return condenseDates(bookedDates, durationMs);
    }

    private List<DateRangeDto> condenseDates(List<DateRangeDto> dates, float durationMs) {
        List<DateRangeDto> condensed = new ArrayList<>();
        DateRangeDto last = null;
        for (DateRangeDto dateRange : dates) {
            if (condensed.isEmpty()) {
                condensed.add(dateRange);
                last = dateRange;
            } else {
                if (dateRange.getStart() - last.getEnd() >= durationMs) {
                    condensed.add(dateRange); // Usable range
                    last = dateRange;
                } else
                    last.setEnd(dateRange.getEnd()); // Not enough time for another booking
            }
        }
        return condensed;
    }

    /**
     * Converts booked dates to available dates.
     * Assumes that:
     * - bookedDates is sorted by start date
     * - all booking intervals intersect the [startDate, endDate] interval
     * - booking intervals don't overlap
     */
    private List<DateRangeDto> convertToAvailable(Long startDate, Long endDate, List<DateRangeDto> bookedDates) {
        List<DateRangeDto> available = new ArrayList<>();
        if (bookedDates.isEmpty()) {
            available.add(new DateRangeDto(startDate, endDate));
            return available;
        }

        if (bookedDates.get(0).getStart() > startDate)
            available.add(new DateRangeDto(startDate, bookedDates.get(0).getStart()));

        for (int i = 1; i < bookedDates.size(); i++) {
            long gapStart = bookedDates.get(i - 1).getEnd();
            long gapEnd = bookedDates.get(i).getStart();
            if (gapStart < gapEnd)
                available.add(new DateRangeDto(gapStart, gapEnd));
        }

        if (bookedDates.get(bookedDates.size() - 1).getEnd() < endDate)
            available.add(new DateRangeDto(bookedDates.get(bookedDates.size() - 1).getEnd(), endDate));

        return available;
    }
}
