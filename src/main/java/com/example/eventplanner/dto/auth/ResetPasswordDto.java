package com.example.eventplanner.dto.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    private String oldPassword;
    @NotNull(message = "New password is required")
    @Length(min = 6, max = 32, message = "New password must be at between 6 and 32 characters")
    private String newPassword;
    private long userId;
}
