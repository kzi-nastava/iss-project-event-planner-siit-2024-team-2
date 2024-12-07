package com.example.eventplanner.dto.serviceproduct.product;

import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.UserRole;

public class ProductMapper {
    public static ProductDto toDto(Product entity) {
        if (entity == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setName(entity.getName());
        dto.setAvailable(entity.isAvailable());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        return dto;
    }
    public static Product toEntity(ProductDto dto, int depth) {
        if (dto == null || depth > 1) {
            return null;
        }
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setAvailable(dto.isAvailable());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        return entity;
    }

    public static CreateProductDto toCreateDto(Product entity, int depth) {
        if (entity == null || depth > 1) {
            return null;
        }
        CreateProductDto dto = new CreateProductDto();
        dto.setName(entity.getName());
        dto.setAvailable(entity.isAvailable());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        return dto;
    }
    public static Product toEntity(CreateProductDto dto) {
        if (dto == null) {
            return null;
        }
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setAvailable(dto.isAvailable());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        return entity;
    }
}
