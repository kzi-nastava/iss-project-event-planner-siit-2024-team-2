package com.example.eventplanner.dto.serviceproduct.service;

import com.example.eventplanner.model.serviceproduct.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceMapper {
    public static ServiceDto toDto(Service service) {
        if (service == null) {
            return null;
        }
        return new ServiceDto(
                service.getId(), service.isActive(), service.getPrice(), service.getDiscount(), service.getName(),
                service.getDescription(), service.getImages().get(0), service.getAvailableEventTypes(), service.getCategory(), service.isAvailable());
    }

    public static Service toEntity(ServiceDto serviceDto) {
        if (serviceDto == null) {
            return null;
        }
        Service service = new Service();
        service.setId(serviceDto.getId());
        service.setActive(serviceDto.isActive());
        service.setPrice(serviceDto.getPrice());
        service.setDiscount(serviceDto.getDiscount());
        service.setName(serviceDto.getName());
        service.setDescription(serviceDto.getDescription());

        List<String> images = new ArrayList<>();
        images.add(serviceDto.getCoverImage());
        service.setImages(images);
        service.setAvailableEventTypes(serviceDto.getAvailableEventTypes());
        service.setCategory(serviceDto.getCategory());
        service.setAvailable(serviceDto.isAvailable());
        return service;
    }
}
