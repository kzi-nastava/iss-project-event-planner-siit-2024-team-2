package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.dto.user.user.EventOrganizerMapper;
import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.UpdateEventOrganizerDto;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        if (eventOrganizerRepository.existsByEmailAndIsActiveTrue(user.getEmail())) return false;
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
        return eventOrganizerRepository.findByIdAndIsActiveTrue(id)
                .map(existing -> {
                    EventOrganizer eventOrganizer = EventOrganizerMapper.toUpdateEntity(eventOrganizerDto);
                    eventOrganizer.setActive(true);
                    eventOrganizer.setId(id);
                    eventOrganizer.setEmail(existing.getEmail());
                    eventOrganizer.setPassword(existing.getPassword());
                    eventOrganizer.setUserRole(UserRole.EVENT_ORGANIZER);
                    eventOrganizer.setFirstName(eventOrganizerDto.getFirstName());
                    eventOrganizer.setLastName(eventOrganizerDto.getLastName());
                    eventOrganizer.setAddress(eventOrganizerDto.getAddress());
                    eventOrganizer.setPhoneNumber(eventOrganizerDto.getPhoneNumber());
                    return EventOrganizerMapper.toUpdateDto(eventOrganizerRepository.save(eventOrganizer));
                }).orElse(null);
    }
}
