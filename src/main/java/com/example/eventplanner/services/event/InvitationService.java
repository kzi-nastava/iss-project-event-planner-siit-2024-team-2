package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.invitation.InvitationDto;
import com.example.eventplanner.dto.event.invitation.InvitationMapper;
import com.example.eventplanner.dto.event.invitation.InvitationNoIdDto;
import com.example.eventplanner.dto.util.EmailDetails;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.Invitation;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.InvitationRepository;
import com.example.eventplanner.services.user.UserService;
import com.example.eventplanner.services.util.EmailFormatUtil;
import com.example.eventplanner.services.util.EmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Value("${frontend.url}")
    private String frontendUrl;

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
        invitation.setToken(dto.getToken());

        return InvitationMapper.toDto(invitationRepository.save(invitation));
    }

    public void sendInvitations(List<Invitation> invitations) {
        for (Invitation invitation : invitations) {
            boolean isRegistered = userService.existsByEmail(invitation.getEmail());
            String inviteLink = frontendUrl + "/accept-invitation?token=" + invitation.getToken();
            String body = EmailFormatUtil.formatInviteEmail(
                    EventMapper.toSummaryDto(invitation.getEvent()),
                    inviteLink,
                    isRegistered);

            emailService.sendMimeMessage(new EmailDetails(
                    invitation.getEmail(),
                    "Event Planner - Invitation to event",
                    body).withHtml(true));
        }
    }
}
