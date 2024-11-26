package com.example.eventplanner.controllers.event;

import com.example.eventplanner.model.event.EventType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EventTypeController {

    @GetMapping(value = "/event-type")
    public ResponseEntity<List<EventType>> getEventTypes(){
        return ResponseEntity.ok(new ArrayList<EventType>());
    }
}
