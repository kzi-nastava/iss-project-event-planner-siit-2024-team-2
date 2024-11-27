package com.example.eventplanner.controllers.event;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.services.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor()
public class EventController {
    private final EventService eventService;

    @GetMapping(value = "/top5")
    public ResponseEntity<List<Event>> getTop5() {
        List<Event> top5 = eventService.getTop5();
        return new ResponseEntity<>(top5, HttpStatus.OK);
    }
}
