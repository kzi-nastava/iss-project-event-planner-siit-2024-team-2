package com.example.eventplanner.repositories.order;

import com.example.eventplanner.model.order.Review;
import com.example.eventplanner.model.utils.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Modifying
    @Query("UPDATE ServiceProductReview e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
    Page<Review> findAllByReviewStatus(ReviewStatus reviewStatus, Pageable pageable);
}
