package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.utils.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Entity {

    private String email;
    private String password;
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private List<User> blockedUsers;
}
