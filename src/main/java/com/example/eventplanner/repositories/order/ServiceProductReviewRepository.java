package com.example.eventplanner.repositories.order;

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

public interface ServiceProductReviewRepository extends JpaRepository<ServiceProductReview, Long> {
    Page<Review> findAllByServiceProductAndReviewStatus(
            ServiceProduct serviceProduct,
            ReviewStatus reviewStatus,
            Pageable pageable);

    @Modifying
    @Query("UPDATE ServiceProductReview e SET e.active = false WHERE e.serviceProduct.id = :serviceProductId")
    void deleteByServiceProduct(@Param("serviceProductId") long serviceProductId);
}
