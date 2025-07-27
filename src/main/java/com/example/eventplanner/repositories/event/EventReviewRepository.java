package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.EventReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
    @Modifying
    @Query("UPDATE EventReview e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
}
