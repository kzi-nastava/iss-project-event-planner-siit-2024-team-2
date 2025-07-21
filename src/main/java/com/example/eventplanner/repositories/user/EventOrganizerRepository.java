package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.user.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    boolean existsByEmail(String email);
}
