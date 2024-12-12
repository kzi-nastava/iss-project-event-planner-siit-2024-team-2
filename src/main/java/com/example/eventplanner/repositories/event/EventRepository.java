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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findTop5ByOrderByDateAsc();
    //List<Event> findAllFiltered(String name, String description, String type, Integer minMaxAttendances, Integer maxMaxAttendances, Boolean open, List<Double> longitudes, List<Double> latitudes, Double maxDistance, Date startDate, Date endDate, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE e.active = true " +
            "AND (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:type IS NULL OR e.type.name = :type) " +
            "AND (:minMaxAttendances IS NULL OR e.maxAttendances >= :minMaxAttendances) " +
            "AND (:maxMaxAttendances IS NULL OR e.maxAttendances <= :maxMaxAttendances) " +
            "AND (:open IS NULL OR e.open = :open) " +
            "AND (:startDate IS NULL OR e.date >= :startDate) " +
            "AND (:endDate IS NULL OR e.date <= :endDate)"
    )
    Page<Event> findAllFiltered(
            Sort sort,
            @Param("name") String name,
            @Param("description") String description,
            @Param("type") String type,
            @Param("minMaxAttendances") Integer minMaxAttendances,
            @Param("maxMaxAttendances") Integer maxMaxAttendances,
            @Param("open") Boolean open,
//            @Param("longitudes")List<Double> longitudes,
//            @Param("latitudes")List<Double> latitudes,
//            @Param("maxDistance")Double maxDistance,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Event e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
}
