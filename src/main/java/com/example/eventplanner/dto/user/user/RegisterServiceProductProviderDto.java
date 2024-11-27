package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterServiceProductProviderDto extends RegisterUserDto{
    private String companyName;
    private String companyDescription;
}
