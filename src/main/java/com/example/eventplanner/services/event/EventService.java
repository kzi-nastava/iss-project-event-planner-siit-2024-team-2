package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    List<Event> events = new ArrayList<>();
    public EventService() {}
}
