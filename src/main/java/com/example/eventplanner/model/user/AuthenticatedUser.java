package com.example.eventplanner.model.user;

import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("active = true")
public class AuthenticatedUser extends BaseUser {
}
