package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.utils.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends Entity {
    private String email;
    private String password;
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    @ManyToMany
    private List<User> blockedUsers;
}
