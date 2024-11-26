package com.example.eventplanner.dto.event;


import com.example.eventplanner.model.event.EventType;

public class EventTypeMapper {

    public static EventTypeDto toDto(EventType eventType) {
        if (eventType == null) {
            return null;
        }
        return new EventTypeDto(
                eventType.getId(),
                eventType.getName(),
                eventType.getRecommendedServiceProducts()
        );
    }

    public static EventType toEntity(EventTypeDto dto) {
        if (dto == null) {
            return null;
        }
        EventType eventType = new EventType(
                dto.getName(),
                dto.getRecommendedServiceProducts()
        );
        eventType.setId(dto.getId());
        eventType.setActive(true);
        return eventType;
    }
}
