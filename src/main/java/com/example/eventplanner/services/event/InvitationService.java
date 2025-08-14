package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.invitation.InvitationDto;
import com.example.eventplanner.dto.event.invitation.InvitationMapper;
import com.example.eventplanner.dto.event.invitation.InvitationNoIdDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.Invitation;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.InvitationRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;

    public List<InvitationDto> getAll() {
        return invitationRepository.findAll()
                .stream()
                .map(InvitationMapper::toDto)
                .toList();
    }

    public InvitationDto getById(long id) {
        return invitationRepository.findById(id)
                .map(InvitationMapper::toDto)
                .orElse(null);
    }

    public InvitationDto getByToken(String token) {
        return invitationRepository.findByToken(token)
                .map(InvitationMapper::toDto)
                .orElse(null);
    }

    public List<InvitationDto> getByEventId(long eventId) {
        return invitationRepository.findByEventId(eventId)
                .stream()
                .map(InvitationMapper::toDto)
                .toList();
    }

    public InvitationDto update(InvitationNoIdDto dto, long id) {
        Invitation invitation = invitationRepository.findById(id).orElse(null);
        if (invitation == null)
            return null;

        Event event = eventRepository.getReferenceById(dto.getEventDto().getId());

        invitation.setEvent(event);
        invitation.setEmail(dto.getEmail());
        invitation.setRegisteredUser(dto.isRegisteredUser());
        invitation.setAccepted(dto.isAccepted());
        invitation.setToken(dto.getToken());

        return InvitationMapper.toDto(invitationRepository.save(invitation));
    }
}
