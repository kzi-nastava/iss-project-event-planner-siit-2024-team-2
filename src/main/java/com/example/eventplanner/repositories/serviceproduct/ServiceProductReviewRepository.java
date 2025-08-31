package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductReview;
import com.example.eventplanner.model.utils.ReviewStatus;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceProductReviewRepository extends JpaRepository<ServiceProductReview, Long> {
    @Modifying
    @Query("UPDATE ServiceProductReview e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
    Page<ServiceProductReview> findAllByReviewStatus(ReviewStatus reviewStatus, Pageable pageable);
    Page<ServiceProductReview> findAllByServiceProductAndReviewStatus(
            ServiceProduct serviceProduct,
            ReviewStatus reviewStatus,
            Pageable pageable);

    @Modifying
    @Query("UPDATE ServiceProductReview e SET e.active = false WHERE e.serviceProduct.id = :serviceProductId")
    void deleteByServiceProduct(@Param("serviceProductId") long serviceProductId);
}
