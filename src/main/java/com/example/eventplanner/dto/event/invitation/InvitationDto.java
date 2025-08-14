package com.example.eventplanner.dto.event.invitation;

import com.example.eventplanner.dto.event.event.EventDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDto {
    private long id;
    private EventDto event;
    private String email;
    private boolean registeredUser;
    private boolean accepted;
    private String token;
}
