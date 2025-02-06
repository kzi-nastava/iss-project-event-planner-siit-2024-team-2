package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.Event;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findTop5ByOrderByDateAsc();
    //List<Event> findAllFiltered(String name, String description, String type, Integer minMaxAttendances, Integer maxMaxAttendances, Boolean open, List<Double> longitudes, List<Double> latitudes, Double maxDistance, Date startDate, Date endDate, PageRequest pageRequest);

    @Query(value = "SELECT e.id, e.active, e.name, e.description, e.type_id, e.maxattendances, e.open, e.latitude, e.longitude, e.date, e.eventorganizer_id " +
            "FROM Event e " +
            "WHERE (:name = '' OR e.name ILIKE CONCAT('%', :name, '%')) " +
            "AND (:description = '' OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (COALESCE(array_length(:types, 1), 0) = 0 OR e.type_id = ANY(:types)) " +
            "AND (:minMaxAttendances IS NULL OR e.maxAttendances >= :minMaxAttendances) " +
            "AND (:maxMaxAttendances IS NULL OR e.maxAttendances <= :maxMaxAttendances) " +
            "AND (:open IS NULL OR e.open = :open) " +
            "AND (e.date >= CAST(:startDate as timestamp)) " +
            "AND (e.date <= CAST(:endDate as timestamp)) " +
            "AND (:maxDistance = 0 " +
            "       OR any_location_within_distance(:latitudes, :longitudes, :maxDistance, e.latitude, e.longitude)" +
            "   )", nativeQuery = true)
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
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Event e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
}
//SELECT e FROM Event e WHERE (? LIKE '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', ?, '%'))) AND (? LIKE '' OR LOWER(e.description) LIKE LOWER(CONCAT('%', ?, '%'))) AND (? IS NULL OR e.type.id in ?) AND (? IS NULL OR e.maxAttendances >= ?) AND (? IS NULL OR e.maxAttendances <= ?) AND (? IS NULL OR e.open = ?) AND (e.date >= CAST(? as timestamp)) AND (e.date <= CAST(? as timestamp)) AND (? = 0        OR CAST(ANY_LOCATION_WITHING_DISTANCE(?, ?, ?, e.latitude, e.longitude) as boolean)   ) order by e.id desc fetch first ? rows only