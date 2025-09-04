package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = """
        WITH filtered_event AS (
            SELECT * FROM event e
            WHERE e.open = true
              AND e.active = true
              AND (:currentUserId IS NULL OR NOT EXISTS
                 (SELECT 1
                  FROM baseuser_baseuser uu
                  WHERE (uu.baseuser_id = :currentUserId
                        AND uu.blockedusers_id = e.eventorganizer_id)
                     OR (uu.baseuser_id = e.eventorganizer_id
                        AND uu.blockedusers_id = :currentUserId)))
        )
        (
            SELECT * FROM filtered_event
            WHERE date >= :currentDate
            ORDER BY date ASC
            LIMIT 5
        )
        UNION ALL
        (
            SELECT * FROM filtered_event
            WHERE date < :currentDate
            ORDER BY date DESC
            LIMIT GREATEST(0, 5 - (
                SELECT COUNT(*) FROM filtered_event
                WHERE date >= :currentDate
            ))
        )
        ORDER BY date ASC
        LIMIT 5
    """, nativeQuery = true)
    List<Event> findTop5(LocalDateTime currentDate, @Param("currentUserId") Long currentUserId);

    @Query(value = "SELECT e.id, e.active, e.name, e.description, e.type_id, e.maxattendances, e.latitude, e.longitude, e.open, e.date, e.eventorganizer_id " +
            "FROM Event e " +
            "WHERE (:organizerId IS NULL OR e.eventorganizer_id = :organizerId)" +
            "AND (:currentUserId IS NULL OR NOT EXISTS " +
            "   (SELECT 1 " +
            "    FROM baseuser_baseuser uu " +
            "    WHERE (uu.baseuser_id = :currentUserId " +
            "          AND uu.blockedusers_id = e.eventorganizer_id) " +
            "       OR (uu.baseuser_id = e.eventorganizer_id " +
            "          AND uu.blockedusers_id = :currentUserId)))" +
            "AND (:name = '' OR e.name ILIKE CONCAT('%', :name, '%')) " +
            "AND (e.active = true) " +
            "AND (:description = '' OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (COALESCE(array_length(:types, 1), 0) = 0 OR e.type_id = ANY(:types)) " +
            "AND (:minMaxAttendances IS NULL OR e.maxAttendances >= :minMaxAttendances) " +
            "AND (:maxMaxAttendances IS NULL OR e.maxAttendances <= :maxMaxAttendances) " +
            "AND (:open IS NULL OR e.open = :open) " +
            "AND (e.date >= CAST(:startDate as timestamp)) " +
            "AND (e.date <= CAST(:endDate as timestamp)) " +
            "AND (:maxDistance = 0 " +
            "       OR any_location_within_distance(:latitudes, :longitudes, :maxDistance, e.latitude, e.longitude))"
            , nativeQuery = true)
    Page<Event> findAllFiltered(
            @Param("name") String name,
            @Param("description") String description,
            @Param("types") Long[] types,
            @Param("minMaxAttendances") Integer minMaxAttendances,
            @Param("maxMaxAttendances") Integer maxMaxAttendances,
            @Param("open") Boolean open,
            @Param("latitudes") Double[] latitudes,
            @Param("longitudes") Double[] longitudes,
            @Param("maxDistance") double maxDistance,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("organizerId") Long organizerId,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Event e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);

    @Query("SELECT MIN(e.maxAttendances), MAX(e.maxAttendances) FROM Event e WHERE e.open = true")
    List<Object[]> findMaxAttendancesRange();

    @Query("SELECT e FROM Event e WHERE e.eventOrganizer.id = :organizerId")
    List<Event> findByOrganizerId(@Param("organizerId") Long organizerId);

    @Query("""
        SELECT e
        FROM Event e
        JOIN e.budgets b
        WHERE b.id = :budgetId
    """)
    Optional<Event> findByBudgetId(Long budgetId);

    @Query("""
        SELECT e
        FROM Event e
        JOIN e.budgets b
        JOIN b.bookings bb
        WHERE bb.id = :bookingId
    """)
    Event findByBookingId(Long bookingId);
}