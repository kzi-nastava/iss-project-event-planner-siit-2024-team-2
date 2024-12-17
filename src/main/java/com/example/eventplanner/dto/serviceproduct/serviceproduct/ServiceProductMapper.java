package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.user.ServiceProductProvider;

import java.util.List;

public class ServiceProductMapper {
    private static ServiceProductDto dto;

    public static ServiceProductDto toDto(ServiceProduct serviceProduct) {
        if (serviceProduct == null)
            return null;
        return new ServiceProductDto(
                serviceProduct.getId(),
                ServiceProductCategoryMapper.toDto(serviceProduct.getCategory()),
                serviceProduct.isAvailable(),
                serviceProduct.isVisible(),
                serviceProduct.getPrice(),
                serviceProduct.getDiscount(),
                serviceProduct.getName(),
                serviceProduct.getDescription(),
                serviceProduct.getImages(),
                serviceProduct.getAvailableEventTypes().stream().map(EventTypeMapper::toDto).toList(),
                UserMapper.toServiceProductProviderDto(serviceProduct.getServiceProductProvider())
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
                ServiceProductCategoryMapper.toDto(serviceProduct.getCategory()),
                serviceProduct.isAvailable(),
                serviceProduct.getPrice(),
                serviceProduct.getDiscount(),
                serviceProduct.getName(),
                serviceProduct.getDescription(),
                UserMapper.toServiceProductProviderDto(serviceProduct.getServiceProductProvider())
        );
    }

    public static ServiceProduct toEntity(ServiceProductNoIdDto dto,
                                          ServiceProductCategory category,
                                          List<EventType> availableEventTypes,
                                          ServiceProductProvider serviceProductProvider) {
        if (dto == null)
            return null;
        return new ServiceProduct(
                category,
                dto.isAvailable(),
                dto.isVisible(),
                dto.getPrice(),
                dto.getDiscount(),
                dto.getName(),
                dto.getDescription(),
                dto.getImages(),
                availableEventTypes,
                serviceProductProvider);
    }
}