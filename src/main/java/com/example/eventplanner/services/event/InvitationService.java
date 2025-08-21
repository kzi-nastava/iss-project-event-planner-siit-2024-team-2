package com.example.eventplanner.services.event;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.invitation.InvitationDto;
import com.example.eventplanner.dto.event.invitation.InvitationMapper;
import com.example.eventplanner.dto.event.invitation.InvitationNoIdDto;
import com.example.eventplanner.dto.util.EmailDetails;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.Invitation;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.AttendanceResult;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.InvitationRepository;
import com.example.eventplanner.services.user.UserService;
import com.example.eventplanner.services.util.EmailFormatUtil;
import com.example.eventplanner.services.util.EmailService;
import com.example.eventplanner.utils.StatusPair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

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
    private final AuthUtil authUtil;
    private final EventService eventService;

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

        Event event = eventRepository.getReferenceById(dto.getEventId());

        invitation.setEvent(event);
        invitation.setEmail(dto.getEmail());

        return InvitationMapper.toDto(invitationRepository.save(invitation));
    }

    public void sendInvitations(List<Invitation> invitations) {
        for (Invitation invitation : invitations) {
            boolean isRegistered = !invitation.isNeedsRegistration();
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

    public StatusPair<InvitationDto> acceptInvitation(String token) {
        BaseUser user = authUtil.getAuthenticatedUser();
        Invitation invitation = invitationRepository.findByToken(token).orElse(null);
        if (invitation == null)
            return new StatusPair<>(null, HttpStatus.NOT_FOUND);
        if (invitation.isAccepted()) // The invitation has already been accepted
            return new StatusPair<>(InvitationMapper.toDto(invitation), HttpStatus.OK);

        if (invitation.isNeedsRegistration()) { // User doesn't exist, create an account for them
            // TODO: Quick registration
        } else if (user != null) { // User is logged in, add them to the event
            if (!user.getEmail().equals(invitation.getEmail()))
                return new StatusPair<>(null, HttpStatus.FORBIDDEN);
            AttendanceResult result = eventService.attend(invitation.getEvent().getId(), user);
            if (result == AttendanceResult.FULL)
                return new StatusPair<>(null, HttpStatus.CONFLICT);
        } else { // User exists, but isn't logged in
            return new StatusPair<>(null, HttpStatus.UNAUTHORIZED);
        }

        invitation.setAccepted(true);

        return new StatusPair<>(InvitationMapper.toDto(invitationRepository.save(invitation)), HttpStatus.OK);
    }
}
