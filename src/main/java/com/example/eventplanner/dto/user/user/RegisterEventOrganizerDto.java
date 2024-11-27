package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.utils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEventOrganizerDto extends RegisterUserDto{
    private String address;
    private String phoneNumber;
}
