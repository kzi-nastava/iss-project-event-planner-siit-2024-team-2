package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.services.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor()
public class EventController {
    private final EventService eventService;
    
    @GetMapping
    public ResponseEntity<Collection<EventDto>> getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer minMaxAttendances,
            @RequestParam(required = false) Integer maxMaxAttendances,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<Double> longitudes,
            @RequestParam(required = false) List<Double> latitudes,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        Collection<EventDto> result = eventService.getAllFilteredPaginated(
                page, size, name, description, type, minMaxAttendances, maxMaxAttendances,
                open, longitudes, latitudes, maxDistance, startDate, endDate);
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
    public ResponseEntity<List<Event>> getTop5() {
        List<Event> top5 = eventService.getTop5();
        return new ResponseEntity<>(top5, HttpStatus.OK);
    }
}
