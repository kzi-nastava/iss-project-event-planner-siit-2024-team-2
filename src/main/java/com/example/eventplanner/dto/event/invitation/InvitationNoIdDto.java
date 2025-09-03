package com.example.eventplanner.dto.event.invitation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationNoIdDto {
    @Min(value = 1, message = "Event is required")
    private long eventId;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
}
