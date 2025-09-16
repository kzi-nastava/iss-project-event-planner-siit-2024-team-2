package com.example.eventplanner.services.event;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.invitation.*;
import com.example.eventplanner.dto.util.EmailDetails;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.Invitation;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.AttendanceResult;
import com.example.eventplanner.model.utils.InvitationResult;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.InvitationRepository;
import com.example.eventplanner.services.communication.NotificationService;
import com.example.eventplanner.services.user.UserService;
import com.example.eventplanner.services.util.EmailFormatUtil;
import com.example.eventplanner.services.util.EmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

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
    private final EventAttendanceService eventAttendanceService;
    private final NotificationService notificationService;

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
            String inviteLink = frontendUrl + "/accept-invitation?token=" + invitation.getToken();
            String body = EmailFormatUtil.formatInviteEmail(
                    EventMapper.toSummaryDto(invitation.getEvent()),
                    inviteLink,
                    invitation.isQuickRegistration());

            emailService.sendMimeMessage(new EmailDetails(
                    invitation.getEmail(),
                    "Event Planner - Invitation to event",
                    body).withHtml(true));
        }
    }

    @Transactional
    public InvitationResult acceptInvitation(String token) {
        BaseUser authenticatedUser = authUtil.getAuthenticatedUser();
        Invitation invitation = invitationRepository.findByToken(token).orElse(null);
        if (invitation == null)
            return new InvitationResult(null, InvitationErrorType.INVITATION_NOT_FOUND);

        try {
            BaseUser emailUser = userService.getUserByEmail(invitation.getEmail());
            return handleExistingUser(invitation, emailUser, authenticatedUser);
        } catch (UsernameNotFoundException ignored) { // User doesn't exist, create an account for them
            return handleQuickRegistration(invitation, authenticatedUser);
        }
    }

    private InvitationResult handleQuickRegistration(Invitation invitation, BaseUser authenticatedUser) {
        if (authenticatedUser != null) // Can't quickly register while someone is logged in
            return new InvitationResult(invitation, InvitationErrorType.FORBIDDEN);
        if (invitation.getEvent() == null)
            return new InvitationResult(null, InvitationErrorType.EVENT_NOT_FOUND);
        // Don't create a new user if the event is full
        if (eventAttendanceService.eventFull(invitation.getEvent().getId(), invitation.getEmail()))
            return new InvitationResult(invitation, InvitationErrorType.EVENT_FULL);
        invitation.setQuickRegistration(true);
        BaseUser newUser = userService.quickRegister(invitation.getEmail());
        notificationService.sendNotification(new NotificationNoIdDto(
                "Welcome!",
                "Welcome to Event Planner! After using an invite link, an account has been created for you. " +
                        "You can now access the event you have been invited to and explore other events on the home page. " +
                        "When you are ready, you can upgrade your account to an Event Organizer or Service Product Provider by " +
                        "using the upgrade button at the top right of the page.",
                false,
                false,
                newUser.getId()
        ), newUser.isMutedNotifications());
        return acceptAndSave(invitation, newUser);
    }

    private InvitationResult handleExistingUser(Invitation invitation, BaseUser emailUser, BaseUser authenticatedUser) {
        invitation.setQuickRegistration(emailUser.getUserRole() == UserRole.AUTHENTICATED);
        if (authenticatedUser == null) // User exists, but isn't logged in
            if (emailUser.getUserRole() == UserRole.AUTHENTICATED)
                return new InvitationResult(invitation, InvitationErrorType.UNAUTHORIZED_QUICK_REGISTRATION);
            else
                return new InvitationResult(invitation, InvitationErrorType.UNAUTHORIZED);

        if (invitation.getEvent() == null)
            return new InvitationResult(null, InvitationErrorType.EVENT_NOT_FOUND);
        if (!authenticatedUser.getEmail().equals(invitation.getEmail()))
            return new InvitationResult(invitation, InvitationErrorType.FORBIDDEN);

        // User is logged in, add them to the event
        return acceptAndSave(invitation, authenticatedUser);
    }

    private InvitationResult acceptAndSave(Invitation invitation, BaseUser user) {
        AttendanceResult attendanceResult = eventAttendanceService.attendEvent(invitation.getEvent().getId(), user);
        if (attendanceResult == AttendanceResult.FULL)
            return new InvitationResult(invitation, user.getUserRole() == UserRole.AUTHENTICATED
                                                        ? InvitationErrorType.EVENT_FULL_QUICK_REGISTRATION
                                                        : InvitationErrorType.EVENT_FULL);
        invitation.setAccepted(true);
        Invitation savedInvitation = invitationRepository.save(invitation);
        return new InvitationResult(savedInvitation, null);
    }
}
