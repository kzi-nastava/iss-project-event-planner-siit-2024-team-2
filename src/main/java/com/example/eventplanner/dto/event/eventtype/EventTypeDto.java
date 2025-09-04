package com.example.eventplanner.dto.event.eventtype;

import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductNameIdDto;
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
    private List<ServiceProductNameIdDto> recommendedServiceProducts;
}
