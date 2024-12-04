package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.user.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {


    Optional<EventOrganizer> findByIdAndIsActiveTrue(long id);

    boolean existsByEmailAndIsActiveTrue(String email);
}
