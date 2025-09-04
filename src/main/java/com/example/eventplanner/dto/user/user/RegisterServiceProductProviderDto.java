package com.example.eventplanner.dto.user.user;

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
public class RegisterServiceProductProviderDto extends RegisterUserDto{
    @NotBlank(message = "Company name is required")
    @Length(min = 1, max = 100, message = "Company name must be between 1 and 100 characters")
    private String companyName;
    @NotBlank(message = "Company description is required")
    @Length(min = 1, max = 1000, message = "Company description must be between 1 and 1000 characters")
    private String companyDescription;
}
