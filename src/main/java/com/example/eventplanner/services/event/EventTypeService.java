package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.model.event.EventType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventTypeService {
    private Map<Long, EventType> eventTypes = new HashMap<>();
    private long idCounter = 0;
    public EventTypeService() {}
    public List<EventTypeDto> getAll() {
        return eventTypes.values().stream().map(EventTypeMapper::toDto).toList();
    }
    public EventTypeDto get(long id) {
        if (!eventTypes.containsKey(id) || !eventTypes.get(id).isActive())
            return null;
        return EventTypeMapper.toDto(eventTypes.get(id));
    }
    public EventTypeDto create(EventTypeDto eventTypeDto) {
        eventTypeDto.setId(++idCounter);
        EventType eventType = EventTypeMapper.toEntity(eventTypeDto);
        eventTypes.put(eventType.getId(), eventType);
        return eventTypeDto;
    }
    public EventTypeDto update(EventTypeDto eventTypeDto, long id) {
        eventTypeDto.setId(id);
        EventType eventType = EventTypeMapper.toEntity(eventTypeDto);
        eventTypes.put(id, eventType);
        return eventTypeDto;
    }
    public boolean delete(long id) {
        if (eventTypes.containsKey(id)) {
            eventTypes.get(id).setActive(false);
            return true;
        }
        return false;
    }
}
