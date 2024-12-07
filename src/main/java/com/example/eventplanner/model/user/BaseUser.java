package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.utils.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseUser extends Entity {
    private String email;
    private String password;
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    @ManyToMany
    private List<BaseUser> blockedUsers;
}
