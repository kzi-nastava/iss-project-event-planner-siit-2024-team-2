package com.example.eventplanner.dto.serviceproduct;

import com.example.eventplanner.model.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceDto {
    private double price;
    private double discount;
    private String name;
    private String description;
    private String coverImage;
    private List<EventType> availableEventTypes;
}
