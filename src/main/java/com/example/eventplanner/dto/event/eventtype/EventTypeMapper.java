package com.example.eventplanner.dto.event.eventtype;


import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.model.serviceproduct.ServiceProductNameIdDto;

import java.util.ArrayList;
import java.util.List;

public class EventTypeMapper {
    private EventTypeMapper() {}

    public static EventTypeDto toDto(EventType eventType) {
        if (eventType == null)
            return null;
        List<ServiceProductNameIdDto> list = new ArrayList<>();
        if (eventType.getRecommendedServiceProducts() != null) {
            for (ServiceProduct serviceProduct: eventType.getRecommendedServiceProducts()) {
                list.add(ServiceProductMapper.toNameDto(serviceProduct));
            }
        }
        return new EventTypeDto(
                eventType.getId(),
                eventType.getName(),
                eventType.getDescription(),
                list
        );
    }

    public static EventType toEntity(EventTypeDto dto) {
        if (dto == null)
            return null;

        EventType eventType = new EventType();
        eventType.setName(dto.getName());
        eventType.setDescription(dto.getDescription());
        eventType.setId(dto.getId());
        eventType.setActive(true);
        return eventType;
    }
    public static EventType toEntity(CreateEventTypeDto dto, List<ServiceProduct> serviceProductList) {
        if (dto == null)
            return null;

        EventType eventType = new EventType(
                dto.getName(),
                dto.getDescription(),
                serviceProductList
        );
        eventType.setActive(true);
        return eventType;
    }

    public static EventTypeSimpleDto toSimpleDto(EventType eventType) {
        if (eventType == null)
            return null;
        return new EventTypeSimpleDto(
                eventType.getId(),
                eventType.getName()
        );
    }
}
