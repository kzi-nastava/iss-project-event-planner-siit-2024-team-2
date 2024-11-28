package com.example.eventplanner.model.serviceproduct;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.user.ServiceProductProvider;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProduct extends Entity {
    private boolean available;
    private double price;
    private String name;
    private String description;
    private ServiceProductProvider serviceProductProvider;
    private List<EventType> availableEventTypes;
    private ServiceProductCategory category;
}
