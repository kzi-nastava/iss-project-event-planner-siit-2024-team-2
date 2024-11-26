package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.EventType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventTypeService {
    List<EventType> eventTypes = new ArrayList<>();
    public EventTypeService() {}

}
