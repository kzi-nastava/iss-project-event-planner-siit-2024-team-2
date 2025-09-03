package com.example.eventplanner.dto.user.userreport;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportNoIdDto {
    @NotBlank(message = "Reported email is required")
    @Email(message = "Email is invalid")
    private String reportedEmail;
    @NotBlank(message = "Reason is required")
    @Length(min = 1, max = 1000, message = "Reason must be between 1 and 1000 characters")
    private String reason;
}
