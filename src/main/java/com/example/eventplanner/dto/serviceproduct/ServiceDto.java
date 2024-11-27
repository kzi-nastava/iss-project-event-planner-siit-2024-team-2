package com.example.eventplanner.dto.serviceproduct;

import java.util.List;

import com.example.eventplanner.model.event.EventType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
	private long id;
    private double price;
    private double discount;
    private String name;
    private String description;
    private String coverImage;
    private List<EventType> availableEventTypes;

    public ServiceDto(CreateServiceDto ServiceDto) {
        this.price = ServiceDto.getPrice();
        this.discount = ServiceDto.getDiscount();
        this.name = ServiceDto.getName();
        this.description = ServiceDto.getDescription();
        this.coverImage = ServiceDto.getCoverImage();
        this.availableEventTypes = ServiceDto.getAvailableEventTypes();
    }
}
