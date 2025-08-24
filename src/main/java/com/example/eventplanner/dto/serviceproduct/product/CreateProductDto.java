package com.example.eventplanner.dto.serviceproduct.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {
    private double price;
    private double discount;
    private String name;
    private String description;
    private List<Long> availableEventTypesIds;
    private Long categoryId;
    private boolean available;
    private boolean visible;
    private long serviceProductProviderId;
    private List<String> images;
}
