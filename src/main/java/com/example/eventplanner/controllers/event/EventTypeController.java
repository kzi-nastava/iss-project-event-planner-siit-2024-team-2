package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.eventtype.CreateEventTypeDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeSimpleDto;
import com.example.eventplanner.services.event.EventTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event-types")
@Validated
public class EventTypeController {

    private final EventTypeService eventTypeService;
    @GetMapping()
    public ResponseEntity<List<EventTypeDto>> getAllEventTypes(){
        return ResponseEntity.ok(eventTypeService.getAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<EventTypeSimpleDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description) {
        Page<EventTypeSimpleDto> result = eventTypeService.getAllFiltered(
                page, size, name, description);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventTypeDto> getEventTypeById(@PathVariable long id){
        EventTypeDto eventTypeDto = eventTypeService.getById(id);
        return eventTypeDto != null ?
                ResponseEntity.ok(eventTypeDto) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<EventTypeDto> createEventType(@Valid @RequestBody CreateEventTypeDto eventTypeDto) {
        EventTypeDto dto = eventTypeService.create(eventTypeDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EventTypeDto> updateEventType(@PathVariable long id, @Valid @RequestBody CreateEventTypeDto eventTypeDto) {
        EventTypeDto eventTypeDto1 = eventTypeService.update(eventTypeDto, id);
        return eventTypeDto1 != null ?
                ResponseEntity.ok(eventTypeDto1) :
                ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<EventTypeDto> deleteEventType(@PathVariable long id){
        boolean success = eventTypeService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
