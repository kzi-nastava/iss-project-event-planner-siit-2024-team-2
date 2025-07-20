package com.example.eventplanner.dto.serviceproduct.product;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.dto.user.user.ServiceProductProviderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDto {
    private ServiceProductProviderDto serviceProductProvider;
    private boolean available;
    private double price;
    private double discount;
    private String name;
    private String description;
    private ServiceProductCategoryDto serviceProductCategoryDto;
    private List<EventTypeDto> eventTypes;
}
