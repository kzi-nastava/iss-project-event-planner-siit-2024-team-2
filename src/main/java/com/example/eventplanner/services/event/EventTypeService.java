package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.eventtype.CreateEventTypeDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventTypeService {
    private final EventTypeRepository eventTypeRepository;
    private final ServiceProductRepository serviceProductRepository;
    public List<EventTypeDto> getAll() {
        return eventTypeRepository.findAll().stream().map(EventTypeMapper::toDto).toList();
    }
    public EventTypeDto getById(long id) {
        return eventTypeRepository.findById(id)
                .map(EventTypeMapper::toDto)
                .orElse(null);
    }
    public EventTypeDto create(CreateEventTypeDto eventTypeDto) {
        List<ServiceProduct> serviceProductList = new ArrayList<>();
        for(long id: eventTypeDto.recommendedServiceProducts) {
            serviceProductList.add(serviceProductRepository.findById(id).orElse(null));
        }
        EventType eventType = EventTypeMapper.toEntity(eventTypeDto, serviceProductList);
        eventTypeRepository.save(eventType);
        return EventTypeMapper.toDto(eventType);
    }

    public EventTypeDto update(CreateEventTypeDto eventTypeDto, long id) {
        List<ServiceProduct> serviceProductList = new ArrayList<>();
        for(long sid: eventTypeDto.recommendedServiceProducts) {
            serviceProductList.add(serviceProductRepository.findById(sid).orElse(null));
        }
        return eventTypeRepository.findById(id)
                .map(existingEventType -> {
                    EventType updatedEventType = EventTypeMapper.toEntity(eventTypeDto, serviceProductList);
                    updatedEventType.setId(id);
                    EventType savedEventType = eventTypeRepository.save(updatedEventType);
                    return EventTypeMapper.toDto(savedEventType);
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        return eventTypeRepository.findById(id)
                .map(eventType -> {
                    eventType.setActive(false);
                    eventTypeRepository.save(eventType);
                    return true;
                })
                .orElse(false);
    }

    public Page<EventTypeDto> getAllFiltered(int page, Integer size, String name, String description) {
        PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);

        Page<EventType> eventTypes = eventTypeRepository.findAllFiltered(pageRequest);

        return eventTypes.map(EventTypeMapper::toDto);
    }
}
