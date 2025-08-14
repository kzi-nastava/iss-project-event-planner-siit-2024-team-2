package com.example.eventplanner.dto.event.invitation;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.Invitation;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;

public class InvitationMapper {
    private InvitationMapper() {}

    public static InvitationDto toDto(Invitation invitation) {
        if (invitation == null)
            return null;

        return new InvitationDto(
                invitation.getId(),
                EventMapper.toDto(invitation.getEvent()),
                invitation.getEmail(),
                invitation.isRegisteredUser(),
                invitation.isAccepted(),
                invitation.getToken()
        );
    }

    public static Invitation toEntity(InvitationNoIdDto dto, Event event) {
        if (dto == null)
            return null;

        return new Invitation(
                event,
                dto.getEmail(),
                dto.isRegisteredUser(),
                dto.isAccepted(),
                dto.getToken()
        );
    }
}
