package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventTypeService {
    private final EventTypeRepository eventTypeRepository;
    public List<EventTypeDto> getAll() {
        return eventTypeRepository.findAll().stream().map(EventTypeMapper::toDto).toList();
    }
    public EventTypeDto getById(long id) {
        return eventTypeRepository.findById(id)
                .filter(EventType::isActive)
                .map(EventTypeMapper::toDto)
                .orElse(null);
    }
    public EventTypeDto create(EventTypeDto eventTypeDto) {
        EventType eventType = EventTypeMapper.toEntity(eventTypeDto);
        EventType savedEventType = eventTypeRepository.save(eventType);
        return EventTypeMapper.toDto(savedEventType);
    }

    public EventTypeDto update(EventTypeDto eventTypeDto, long id) {
        return eventTypeRepository.findById(id)
                .filter(EventType::isActive)
                .map(existingEventType -> {
                    EventType updatedEventType = EventTypeMapper.toEntity(eventTypeDto);
                    updatedEventType.setId(id);
                    EventType savedEventType = eventTypeRepository.save(updatedEventType);
                    return EventTypeMapper.toDto(savedEventType);
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        return eventTypeRepository.findById(id)
                .filter(EventType::isActive)
                .map(eventType -> {
                    eventType.setActive(false);
                    eventTypeRepository.save(eventType);
                    return true;
                })
                .orElse(false);
    }

}
