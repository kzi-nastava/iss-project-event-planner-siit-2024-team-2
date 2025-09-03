package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.utils.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    private long id;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "Password is required")
    @Range(min = 6, max = 32, message = "Password must be at between 6 and 32 characters")
    private String password;
    @NotBlank(message = "First name is required")
    @Length(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Length(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;
    @NotBlank(message = "Address is required")
    @Length(min = 1, max = 100, message = "Address must be between 1 and 100 characters")
    private String address;
    @NotBlank(message = "Phone number is required")
    @Length(min = 1, max = 100, message = "Phone number must be between 1 and 100 characters")
    private String phoneNumber;
    @NotNull(message = "User role is required")
    private UserRole userRole;
    private String image = null;
    private String imageEncodedName = null;
}
