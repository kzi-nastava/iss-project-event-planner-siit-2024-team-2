package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventReview;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
@Setter
public class EventService {
    Map<Long, Event> events = new HashMap<>();
    public EventService() {}

    public List<Event> getTop5() {
        return events.values()
                .stream()
                .sorted(Comparator.comparing(Event::getDate))
                .limit(5)
                .toList();
    }
}
