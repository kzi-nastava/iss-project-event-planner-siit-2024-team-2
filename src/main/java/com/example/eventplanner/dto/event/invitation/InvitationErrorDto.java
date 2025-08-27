package com.example.eventplanner.dto.event.invitation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvitationErrorDto {
    private InvitationErrorType type;
    private Long eventId = null;

    public InvitationErrorDto(InvitationErrorType type) {
        this.type = type;
    }
}
