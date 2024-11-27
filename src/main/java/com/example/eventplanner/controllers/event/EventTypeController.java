package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.services.event.EventTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event-types")
public class EventTypeController {

    private final EventTypeService eventTypeService;
    @GetMapping()
    public ResponseEntity<List<EventTypeDto>> getAllEventTypes(){
        return ResponseEntity.ok(eventTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventTypeDto> getEventTypeById(@PathVariable long id){
        EventTypeDto eventTypeDto = eventTypeService.get(id);
        if(eventTypeDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventTypeDto);
    }

    @PostMapping()
    public ResponseEntity<EventTypeDto> createEventType(@RequestBody EventTypeDto eventTypeDto){
        return ResponseEntity.ok(eventTypeService.create(eventTypeDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<EventTypeDto> updateEventType(@PathVariable long id, @RequestBody EventTypeDto eventTypeDto){
        EventTypeDto eventTypeDto1 = eventTypeService.update(eventTypeDto, id);
        if (eventTypeDto1 != null) {
            return ResponseEntity.ok(eventTypeDto1);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<EventTypeDto> deleteEventType(@PathVariable long id){
        boolean success = eventTypeService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
