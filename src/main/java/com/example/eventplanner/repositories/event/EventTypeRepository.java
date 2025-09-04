package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    @Query("SELECT e FROM EventType e ")
    Page<EventType> findAllFiltered(
                                 Pageable pageable);
}
