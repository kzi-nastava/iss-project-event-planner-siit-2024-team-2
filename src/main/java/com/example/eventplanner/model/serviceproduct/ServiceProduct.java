package com.example.eventplanner.model.serviceproduct;

import java.util.List;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.user.ServiceProductProvider;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProduct extends Entity {
	private ServiceProductCategory category;
    private boolean available;
    private boolean visible;
    private double price;
    private double discount;
    private String name;
    private String description;
    private List<String> images;
    private List<EventType> availableEventTypes;
    private ServiceProductProvider serviceProductProvider;
}
