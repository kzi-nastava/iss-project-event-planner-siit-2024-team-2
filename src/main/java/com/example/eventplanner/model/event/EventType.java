package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventType extends Entity {
    private String name;
    private List<ServiceProduct> recommendedServiceProducts;
}
