package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductDto {
    private long id;
    private long categoryId;
    private boolean available;
    private boolean visible;
    private double price;
    private double discount;
    private String name;
    private String description;
    private List<String> images;
    private List<Long> availableEventTypeIds;
    private long serviceProductProviderId;
}
