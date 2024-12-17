package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.ServiceProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceProductReviewRepository extends JpaRepository<ServiceProductReview, Long> {
    @Modifying
    @Query("UPDATE ServiceProductReview e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
}
