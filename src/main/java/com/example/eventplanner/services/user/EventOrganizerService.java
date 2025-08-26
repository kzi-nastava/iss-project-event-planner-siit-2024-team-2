package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.user.EventOrganizerMapper;
import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.UpdateEventOrganizerDto;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventOrganizerService {
    private final EventOrganizerRepository eventOrganizerRepository;

    public RegisterEventOrganizerDto getEventOrganizerById(long id) {
        return eventOrganizerRepository.findById(id)
                .map(EventOrganizerMapper::toDto)
               .orElse(null);
    }

    public UpdateEventOrganizerDto updateEventOrganizer(long id, UpdateEventOrganizerDto eventOrganizerDto) {
        return eventOrganizerRepository.findById(id)
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

    public EventOrganizer findByUsername(String username) {
        return eventOrganizerRepository.findByEmail(username).orElse(null);
    }
}
