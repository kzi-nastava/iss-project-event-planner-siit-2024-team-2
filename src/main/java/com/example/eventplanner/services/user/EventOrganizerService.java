package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.user.EventOrganizerMapper;
import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.UpdateEventOrganizerDto;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventOrganizerService {
    private final EventOrganizerRepository eventOrganizerRepository;

    public boolean registerEventOrganizer(RegisterEventOrganizerDto registerEventOrganizerDto) {
        if (!validateEventOrganizer(registerEventOrganizerDto))
            return false;
        eventOrganizerRepository.save(EventOrganizerMapper.toEntity(registerEventOrganizerDto));
        return true;
    }

    private boolean validateEventOrganizer(RegisterEventOrganizerDto user) {
        if (user == null) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty()) return false;
        if (eventOrganizerRepository.existsByEmail(user.getEmail())) return false;
        if (user.getPassword() == null || user.getPassword().length() < 6) return false;
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) return false;
        if (user.getLastName() == null || user.getLastName().isEmpty()) return false;
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10,15}")) return false;
        return true;
    }

    public RegisterEventOrganizerDto getEventOrganizerById(long id) {
        return eventOrganizerRepository.findByIdAndIsActiveTrue(id)
                .map(EventOrganizerMapper::toDto)
               .orElse(null);
    }

    public UpdateEventOrganizerDto updateEventOrganizer(long id, UpdateEventOrganizerDto eventOrganizerDto) {
        EventOrganizer user = (EventOrganizer) users.get(id);
        if (user == null || !user.isActive() || user.getUserRole() != UserRole.EVENT_ORGANIZER) {
            return null;
        }
        user.setFirstName(eventOrganizerDto.getFirstName());
        user.setLastName(eventOrganizerDto.getLastName());
        user.setAddress(eventOrganizerDto.getAddress());
        user.setPhoneNumber(eventOrganizerDto.getPhoneNumber());
        return UserMapper.toUpdateDto(user);
    }
}
