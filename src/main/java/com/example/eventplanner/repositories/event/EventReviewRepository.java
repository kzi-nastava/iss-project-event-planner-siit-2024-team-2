package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.EventReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
}
