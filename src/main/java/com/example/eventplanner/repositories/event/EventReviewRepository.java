package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.EventReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
}
