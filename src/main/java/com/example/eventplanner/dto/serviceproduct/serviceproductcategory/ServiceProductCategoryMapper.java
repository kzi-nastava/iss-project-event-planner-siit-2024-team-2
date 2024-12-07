package com.example.eventplanner.dto.serviceproduct.serviceproductcategory;

import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;

public class ServiceProductCategoryMapper {
    public static ServiceProductCategoryDto toDto(ServiceProductCategory entity) {
        if (entity == null) {
            return null;
        }
        ServiceProductCategoryDto dto = new ServiceProductCategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
    public static ServiceProductCategory toEntity(ServiceProductCategoryDto dto) {
        if (dto == null) {
            return null;
        }
        ServiceProductCategory entity = new ServiceProductCategory();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setActive(true);
        return entity;
    }

    public static ServiceProductCategory toEntity(ServiceProductCategoryNoIdDto dto) {
        if (dto == null) {
            return null;
        }
        ServiceProductCategory entity = new ServiceProductCategory();
        entity.setName(dto.getName());
        entity.setActive(true);
        return entity;
    }
}
