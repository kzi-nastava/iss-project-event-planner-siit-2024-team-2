package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.user.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    boolean existsByEmail(String email);

    Optional<EventOrganizer> findByEmail(String username);

    @Query("""
        SELECT CASE WHEN EXISTS (
            FROM EventOrganizer eo
            JOIN Event e ON eo.id = e.eventOrganizer.id
            JOIN e.budgets b
            JOIN b.purchases p
            WHERE eo.id = :organizerId
              AND p.product.id = :productId
            ) THEN true ELSE false END
        """)
    boolean hasPurchased(
            @Param("organizerId") long organizerId,
            @Param("productId") long productId);

    @Query("""
        SELECT CASE WHEN EXISTS (
            FROM EventOrganizer eo
            JOIN Event e ON eo.id = e.eventOrganizer.id
            JOIN e.budgets b
            JOIN b.bookings bb
            WHERE eo.id = :organizerId
              AND bb.service.id = :serviceId
              AND bb.status = 0
              ) THEN true ELSE false END
        """)
    boolean hasBooked(
            @Param("organizerId") long organizerId,
            @Param("serviceId") long serviceId);

    @Query("""
        SELECT eo
        FROM EventOrganizer eo
        JOIN Event e ON eo.id = e.eventOrganizer.id
        JOIN e.budgets b
        JOIN b.bookings bb
        WHERE bb.id = :bookingId
        """)
    EventOrganizer findByBookingId(long bookingId);
}
