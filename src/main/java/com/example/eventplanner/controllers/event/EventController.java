package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.services.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor()
public class EventController {
    private final EventService eventService;
    
    @GetMapping
    public ResponseEntity<Page<EventDto>> getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) Integer minMaxAttendances,
            @RequestParam(required = false) Integer maxMaxAttendances,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<Double> latitudes,
            @RequestParam(required = false) List<Double> longitudes,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<EventDto> result = eventService.getAllFiltered(
                EventDto.class,
                page, size, sort, name, description, types, minMaxAttendances, maxMaxAttendances,
                open, latitudes, longitudes, maxDistance, startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/summaries")
    public ResponseEntity<Page<EventSummaryDto>> getEventSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) Integer minMaxAttendances,
            @RequestParam(required = false) Integer maxMaxAttendances,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<Double> latitudes,
            @RequestParam(required = false) List<Double> longitudes,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<EventSummaryDto> result = eventService.getAllFiltered(
                EventSummaryDto.class,
                page, size, sort, name, description, types, minMaxAttendances, maxMaxAttendances,
                open, latitudes, longitudes, maxDistance, startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") Long id) {
        EventDto result = eventService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventNoIdDto dto) {
        EventDto result = eventService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable("id") Long id, @RequestBody EventNoIdDto dto) {
        EventDto result = eventService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EventDto> deleteEvent(@PathVariable("id") Long id) {
        boolean success = eventService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/top5")
    public ResponseEntity<Collection<EventSummaryDto>> getTop5() {
        Collection<EventSummaryDto> result = eventService.getTop5();
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/create-agenda")
    public ResponseEntity<List<ActivityDto>> createAgenda(@PathVariable long id, @RequestBody List<ActivityDto> activities) {
        boolean success = eventService.createAgenda(id, activities);
        return success
                ? ResponseEntity.ok(activities)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/{id}/purchases")
    public ResponseEntity<List<PurchaseDto>> getPurchases(@PathVariable("id") Long id) {
        List<PurchaseDto> result = eventService.getPurchases(id);
        return result != null ?
            new ResponseEntity<>(result, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getBookings(@PathVariable("id") Long id) {
        List<BookingDto> result = eventService.getBookings(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
