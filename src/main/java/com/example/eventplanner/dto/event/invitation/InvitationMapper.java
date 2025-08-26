package com.example.eventplanner.dto.event.invitation;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.Invitation;

public class InvitationMapper {
    private InvitationMapper() {}

    public static InvitationDto toDto(Invitation invitation, boolean justRegistered) {
        if (invitation == null)
            return null;

        return new InvitationDto(
                invitation.getId(),
                EventMapper.toDto(invitation.getEvent()),
                invitation.getEmail(),
                invitation.getToken(),
                invitation.isAccepted(),
                invitation.isQuickRegistration(),
                justRegistered
        );
    }
    public static InvitationDto toDto(Invitation invitation) {
        return toDto(invitation, false);
    }

    public static Invitation toEntity(InvitationNoIdDto dto, Event event) {
        if (dto == null)
            return null;

        return new Invitation(
                event,
                dto.getEmail()
        );
    }
}
