package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {
    //private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static RegisterUserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        RegisterEventOrganizerDto dto = new RegisterEventOrganizerDto();
        dto.setId(entity.getId());
        dto.setPassword(entity.getPassword());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUserRole(entity.getUserRole());
        return dto;
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
    public static RegisterEventOrganizerDto toDto(EventOrganizer entity) {
        if (entity == null) {
            return null;
        }
        RegisterEventOrganizerDto dto = new RegisterEventOrganizerDto();
        dto.setId(entity.getId());
        dto.setPassword(entity.getPassword());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUserRole(UserRole.EVENT_ORGANIZER);
        return dto;
    }
    public static EventOrganizer toEntity(RegisterEventOrganizerDto dto) {
        if (dto == null) {
            return null;
        }
        EventOrganizer entity = new EventOrganizer();
        entity.setId(dto.getId());
        entity.setActive(true);
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setUserRole(UserRole.EVENT_ORGANIZER);
        return entity;
    }

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

}
