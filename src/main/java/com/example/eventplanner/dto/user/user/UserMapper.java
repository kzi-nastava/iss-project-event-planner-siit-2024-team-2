package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;

public class UserMapper {
    //private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static RegisterUserDto toDto(BaseUser entity) {
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

    public static BaseUser toEntity(RegisterUserDto dto) {
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


    public static RegisterUserDto toDto(RegisterUserDto entity) {
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
                user.getPhoneNumber()
        );
    }
}
