package com.example.eventplanner.dto.event.eventtype;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class EventTypeDto {
    public long id;
    public String name;
    public List<ServiceProduct> recommendedServiceProducts;
}
