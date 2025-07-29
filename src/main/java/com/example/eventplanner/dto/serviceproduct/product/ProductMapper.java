package com.example.eventplanner.dto.serviceproduct.product;

import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;

import com.example.eventplanner.model.user.ServiceProductProvider;

import java.util.List;

public class ProductMapper {
    private ProductMapper() {}

    public static ProductDto toDto(Product entity) {
        if (entity == null)
            return null;

        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAvailable(entity.isAvailable());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDiscount(entity.getDiscount());
        dto.setServiceProductProvider(UserMapper.toServiceProductProviderDto(entity.getServiceProductProvider()));
        return dto;
    }

    public static ProductDetailsDto toDetailsDto(Product entity) {
        if (entity == null) {
            return null;
        }
        ProductDetailsDto dto = new ProductDetailsDto();
        dto.setName(entity.getName());
        dto.setAvailable(entity.isAvailable());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDiscount(entity.getDiscount());
        dto.setServiceProductProvider(UserMapper.toServiceProductProviderDto(entity.getServiceProductProvider()));
        dto.setServiceProductCategoryDto(ServiceProductCategoryMapper.toDto(entity.getCategory()));
        dto.setEventTypes(entity.getAvailableEventTypes().stream().map(EventTypeMapper::toDto).toList());
        return dto;
    }

    public static Product toEntity(ProductDto dto, ServiceProductProvider spp) {
        if (dto == null)
            return null;

        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setAvailable(dto.isAvailable());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setDiscount(dto.getDiscount());
        entity.setServiceProductProvider(spp);
        return entity;
    }

    public static CreateProductDto toCreateDto(Product entity) {
        if (entity == null)
            return null;

        CreateProductDto dto = new CreateProductDto();
        dto.setName(entity.getName());
        dto.setAvailable(entity.isAvailable());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDiscount(entity.getDiscount());
        return dto;
    }
    public static Product toEntity(CreateProductDto dto,
                                   ServiceProductProvider serviceProductProvider,
                                   ServiceProductCategory serviceProductCategory,
                                   List<EventType> eventTypeList) {
        if (dto == null) {
            return null;
        }
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setAvailable(dto.isAvailable());
        entity.setVisible(dto.isVisible());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setDiscount(dto.getDiscount());
        entity.setServiceProductProvider(serviceProductProvider);
        entity.setCategory(serviceProductCategory);
        entity.setAvailableEventTypes(eventTypeList);
        return entity;
    }
}
