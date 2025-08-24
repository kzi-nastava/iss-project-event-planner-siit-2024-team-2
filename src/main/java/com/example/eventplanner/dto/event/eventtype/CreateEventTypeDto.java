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
public class CreateEventTypeDto {
    public String name;
    public String description;
    public List<Long> recommendedServiceProducts;
}
