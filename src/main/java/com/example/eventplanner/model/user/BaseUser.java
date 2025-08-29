package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.utils.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.Date;
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
    private String image;
    @ManyToMany
    private List<BaseUser> blockedUsers;
    @Transient
    private String jwt;
    @ManyToMany
    @JoinTable(name = "event_attendances",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> attendingEvents;
    private Instant suspendedAt = null;
}
