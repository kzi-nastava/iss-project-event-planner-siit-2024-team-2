package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    boolean existsByEmail(String email);

    Optional<EventOrganizer> findByEmail(String username);
}
