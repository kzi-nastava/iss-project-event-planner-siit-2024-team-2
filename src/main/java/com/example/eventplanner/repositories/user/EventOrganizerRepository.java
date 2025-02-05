package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.event.EventCreatorProjection;
import com.example.eventplanner.model.user.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    boolean existsByEmail(String email);

    @Query(value = "SELECT e.id AS eventId, u.name AS creatorUsername, u.email AS creatorEmail " +
            "FROM Event e " +
            "JOIN BaseUser u ON e.creator_id = u.id " +
            "WHERE e.id IN (:eventIds)",
            nativeQuery = true)
    List<EventCreatorProjection> findCreatorsByEventIds(@Param("eventIds") List<Long> eventIds);
}
