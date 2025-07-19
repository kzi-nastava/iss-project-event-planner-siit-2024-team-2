package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.event.EventCreatorProjection;
import com.example.eventplanner.model.user.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    boolean existsByEmail(String email);
}
