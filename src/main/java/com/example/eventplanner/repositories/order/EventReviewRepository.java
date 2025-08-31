package com.example.eventplanner.repositories.order;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.EventReview;
import com.example.eventplanner.model.order.Review;
import com.example.eventplanner.model.order.ServiceProductReview;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.utils.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
    Page<Review> findAllByEventAndReviewStatus(
            Event event,
            ReviewStatus reviewStatus,
            Pageable pageable);

    @Modifying
    @Query("UPDATE EventReview e SET e.active = false WHERE e.event.id = :eventId")
    void deleteByEvent(@Param("eventId") long eventId);
}
