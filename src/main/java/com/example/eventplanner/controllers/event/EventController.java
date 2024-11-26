package com.example.eventplanner.controllers.event;

import com.example.eventplanner.services.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor()
public class EventController {
    private final EventService eventService;
}
