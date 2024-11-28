package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;

public class ServiceProductMapper {
    public static ServiceProductDto toDto(ServiceProduct serviceProduct) {
        if (serviceProduct == null)
            return null;
        return new ServiceProductDto(
                serviceProduct.getId(),
                serviceProduct.getCategory().getId(),
                serviceProduct.isAvailable(),
                serviceProduct.isVisible(),
                serviceProduct.getPrice(),
                serviceProduct.getDiscount(),
                serviceProduct.getName(),
                serviceProduct.getDescription(),
                serviceProduct.getImages(),
                serviceProduct.getAvailableEventTypes().stream().map(EventType::getId).toList(),
                serviceProduct.getServiceProductProvider().getId()
        );
    }

    public static ServiceProductNoIdDto toDtoNoId(ServiceProduct serviceProduct) {
        if (serviceProduct == null)
            return null;
        return new ServiceProductNoIdDto(
                serviceProduct.getCategory().getId(),
                serviceProduct.isAvailable(),
                serviceProduct.isVisible(),
                serviceProduct.getPrice(),
                serviceProduct.getDiscount(),
                serviceProduct.getName(),
                serviceProduct.getDescription(),
                serviceProduct.getImages(),
                serviceProduct.getAvailableEventTypes().stream().map(EventType::getId).toList(),
                serviceProduct.getServiceProductProvider().getId()
        );
    }

    public static ServiceProductSummaryDto toSummaryDto(ServiceProduct serviceProduct) {
        if (serviceProduct == null)
            return null;
        return new ServiceProductSummaryDto(
                serviceProduct.getId(),
                serviceProduct.getCategory().getId(),
                serviceProduct.isAvailable(),
                serviceProduct.getPrice(),
                serviceProduct.getDiscount(),
                serviceProduct.getName(),
                serviceProduct.getDescription(),
                serviceProduct.getServiceProductProvider().getId()
        );
    }

    public static ServiceProduct toEntity(ServiceProductDto dto) {
        if (dto == null)
            return null;
        ServiceProduct serviceProduct = new ServiceProduct(
                null,
                dto.isAvailable(),
                dto.isVisible(),
                dto.getPrice(),
                dto.getDiscount(),
                dto.getName(),
                dto.getDescription(),
                dto.getImages(),
                null,
                null);
        serviceProduct.setId(dto.getId());
        serviceProduct.setActive(true);
        return serviceProduct;
    }

    public static ServiceProduct toEntity(ServiceProductNoIdDto dto) {
        if (dto == null)
            return null;
        ServiceProduct serviceProduct = new ServiceProduct(
                null,
                dto.isAvailable(),
                dto.isVisible(),
                dto.getPrice(),
                dto.getDiscount(),
                dto.getName(),
                dto.getDescription(),
                dto.getImages(),
                null,
                null);
        serviceProduct.setActive(true);
        return serviceProduct;
    }
}