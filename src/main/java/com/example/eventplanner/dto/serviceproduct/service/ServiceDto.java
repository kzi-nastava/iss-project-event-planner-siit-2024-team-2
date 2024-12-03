package com.example.eventplanner.dto.serviceproduct.service;

import java.util.List;

import com.example.eventplanner.model.event.EventType;

import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
	private Long id;
    private boolean isActive = true;
    private double price;
    private double discount;
    private String name;
    private String description;
    private String coverImage;
    private List<EventType> availableEventTypes;
    private ServiceProductCategory category;
    private boolean available;

    public ServiceDto(Long id, CreateServiceDto ServiceDto) {
        this.id = id;
        this.price = ServiceDto.getPrice();
        this.discount = ServiceDto.getDiscount();
        this.name = ServiceDto.getName();
        this.description = ServiceDto.getDescription();
        this.coverImage = ServiceDto.getCoverImage();
        this.availableEventTypes = ServiceDto.getAvailableEventTypes();
        this.category = ServiceDto.getCategory();
        this.available = ServiceDto.isAvailable();
    }
}
