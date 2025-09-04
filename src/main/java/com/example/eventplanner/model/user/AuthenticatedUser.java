package com.example.eventplanner.model.user;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("active = true")
@Entity
public class AuthenticatedUser extends BaseUser {
}
