package com.example.eventplanner.dto.event;

import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class EventTypeDto {
    public long id;
    public String name;
    public List<ServiceProduct> recommendedServiceProducts;

    public EventTypeDto(EventType eventType) {
        name = eventType.getName();
        recommendedServiceProducts = eventType.getRecommendedServiceProducts();
        id = eventType.getId();
    }

    public EventTypeDto(long id, String name, List<ServiceProduct> recommendedServiceProducts) {
        this.name = name;
        this.recommendedServiceProducts = recommendedServiceProducts;
        this.id = id;
    }
}
