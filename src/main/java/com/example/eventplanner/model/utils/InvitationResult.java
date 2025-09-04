package com.example.eventplanner.model.utils;

import com.example.eventplanner.dto.event.invitation.InvitationDto;
import com.example.eventplanner.dto.event.invitation.InvitationErrorType;
import com.example.eventplanner.model.event.Invitation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InvitationResult {
    Invitation invitation;
    InvitationErrorType error;
}
