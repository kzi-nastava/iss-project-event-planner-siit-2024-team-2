package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.UserRole;

public class EventOrganizerMapper {

    public static UpdateEventOrganizerDto toUpdateDto(EventOrganizer entity) {
        if (entity == null) {
            return null;
        }
        UpdateEventOrganizerDto dto = new UpdateEventOrganizerDto();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        return dto;
    }

    public static EventOrganizer toUpdateEntity(UpdateEventOrganizerDto dto) {
        if (dto == null) {
            return null;
        }
        EventOrganizer entity = new EventOrganizer();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        return entity;
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
}
