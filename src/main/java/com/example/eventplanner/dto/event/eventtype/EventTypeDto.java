package com.example.eventplanner.dto.event.eventtype;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventTypeDto {
    private long id;
    private String name;
    private String description;
    private List<ServiceProduct> recommendedServiceProducts;
}
