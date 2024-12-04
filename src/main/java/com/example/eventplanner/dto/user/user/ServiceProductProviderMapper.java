package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.UserRole;

public class ServiceProductProviderMapper {
    public static ServiceProductProvider toEntity(RegisterServiceProductProviderDto dto) {
        if (dto == null) {
            return null;
        }
        ServiceProductProvider entity = new ServiceProductProvider();
        entity.setId(dto.getId());
        entity.setActive(true);
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setUserRole(UserRole.SERVICE_PRODUCT_PROVIDER);
        entity.setCompanyName(dto.getCompanyName());
        entity.setCompanyDescription(dto.getCompanyDescription());
        return entity;
    }

    public static UpdateServiceProductProviderDto toUpdateDto(ServiceProductProvider entity) {
        if (entity == null) {
            return null;
        }
        UpdateServiceProductProviderDto dto = new UpdateServiceProductProviderDto();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setCompanyDescription(entity.getCompanyDescription());
        return dto;
    }

    public static ServiceProductProvider toUpdateEntity(UpdateServiceProductProviderDto dto) {
        if (dto == null) {
            return null;
        }
        ServiceProductProvider entity = new ServiceProductProvider();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setCompanyDescription(dto.getCompanyDescription());
        return entity;
    }

    public static RegisterServiceProductProviderDto toDto(ServiceProductProvider entity) {
        if (entity == null) {
            return null;
        }
        RegisterServiceProductProviderDto dto = new RegisterServiceProductProviderDto();
        dto.setId(entity.getId());
        dto.setPassword(entity.getPassword());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUserRole(UserRole.SERVICE_PRODUCT_PROVIDER);
        dto.setCompanyName(entity.getCompanyName());
        dto.setCompanyDescription(entity.getCompanyDescription());
        return dto;
    }
}
