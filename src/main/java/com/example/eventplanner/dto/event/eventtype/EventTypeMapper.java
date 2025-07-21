package com.example.eventplanner.dto.event.eventtype;


import com.example.eventplanner.model.event.EventType;

public class EventTypeMapper {
    private EventTypeMapper() {}

    public static EventTypeDto toDto(EventType eventType) {
        if (eventType == null)
            return null;

        return new EventTypeDto(
                eventType.getId(),
                eventType.getName(),
                eventType.getDescription(),
                eventType.getRecommendedServiceProducts()
        );
    }

    public static EventType toEntity(EventTypeDto dto) {
        if (dto == null)
            return null;

        EventType eventType = new EventType(
                dto.getName(),
                dto.getDescription(),
                dto.getRecommendedServiceProducts()
        );
        eventType.setId(dto.getId());
        eventType.setActive(true);
        return eventType;
    }
    public static EventType toEntity(CreateEventTypeDto dto) {
        if (dto == null)
            return null;

        EventType eventType = new EventType(
                dto.getName(),
                dto.getDescription(),
                dto.getRecommendedServiceProducts()
        );
        eventType.setActive(true);
        return eventType;
    }
}
