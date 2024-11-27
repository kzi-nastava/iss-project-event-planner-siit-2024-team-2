package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.services.event.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    private final EventTypeService eventTypeService;
    @Autowired
    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }
    @GetMapping()
    public ResponseEntity<List<EventTypeDto>> getAll(){
        return ResponseEntity.ok(eventTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventTypeDto> getById(@PathVariable long id){
        EventTypeDto eventTypeDto = eventTypeService.getById(id);
        if(eventTypeDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventTypeDto);
    }

    @PostMapping()
    public ResponseEntity<EventTypeDto> create(@RequestBody EventTypeDto eventTypeDto){
        return ResponseEntity.ok(eventTypeService.create(eventTypeDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<EventTypeDto> update(@PathVariable long id, @RequestBody EventTypeDto eventTypeDto){
        EventTypeDto eventTypeDto1 = eventTypeService.update(eventTypeDto, id);
        if (eventTypeDto1 != null) {
            return ResponseEntity.ok(eventTypeDto1);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<EventTypeDto> delete(@PathVariable long id){
        boolean success = eventTypeService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
