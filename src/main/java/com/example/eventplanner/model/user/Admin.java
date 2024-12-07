package com.example.eventplanner.model.user;

import jakarta.persistence.Entity;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("active = true")
@Entity
public class Admin extends BaseUser {
}
