package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.services.serviceproduct.ImageService;

public class UserMapper {
    private UserMapper() {}

    public static RegisterUserDto toDto(BaseUser entity) {
        if (entity == null)
            return null;

        RegisterEventOrganizerDto dto = new RegisterEventOrganizerDto();
        dto.setId(entity.getId());
        dto.setPassword(entity.getPassword());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUserRole(entity.getUserRole());
        dto.setImage(entity.getImage());
        dto.setImageEncodedName(ImageService.encodePath(entity.getImage()));
        return dto;
    }

    public static BaseUser toEntity(RegisterUserDto dto) {
        if (dto == null)
            return null;

        EventOrganizer entity = new EventOrganizer();
        entity.setId(dto.getId());
        entity.setActive(true);
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setUserRole(dto.getUserRole());
        entity.setImage(dto.getImage());
        return entity;
    }

    public static BaseUser toEntity(RegisterServiceProductProviderDto dto) {
        if (dto == null)
            return null;

        ServiceProductProvider entity = new ServiceProductProvider();
        entity.setId(dto.getId());
        entity.setActive(true);
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setCompanyName(dto.getCompanyName());
        entity.setCompanyDescription(dto.getCompanyDescription());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setUserRole(dto.getUserRole());
        entity.setImage(dto.getImage());
        return entity;
    }

    public static BaseUserDto toBaseUserDto(BaseUser user) {
        if (user == null)
            return null;

        return new BaseUserDto(
                user.getId(),
                user.getEmail(),
                user.getUserRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getImage(),
                ImageService.encodePath(user.getImage()),
                user.isMutedNotifications()
        );
    }

    public static ServiceProductProviderDto toServiceProductProviderDto(ServiceProductProvider spp) {
        if (spp == null)
            return null;

        return new ServiceProductProviderDto(
                spp.getId(),
                spp.getEmail(),
                spp.getUserRole(),
                spp.getFirstName(),
                spp.getLastName(),
                spp.getAddress(),
                spp.getPhoneNumber(),
                spp.getCompanyName(),
                spp.getCompanyDescription(),
                spp.getImage(),
                ImageService.encodePath(spp.getImage())
        );
    }
}
