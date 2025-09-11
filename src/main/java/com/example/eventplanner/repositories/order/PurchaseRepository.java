package com.example.eventplanner.repositories.order;

import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Modifying
    @Query("UPDATE Purchase e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);

    @Query("SELECT p FROM Purchase p WHERE p.product.serviceProductProvider.id = :providerId")
    List<Purchase> findByProviderId(@Param("providerId") Long providerId);

    @Query("SELECT p FROM Purchase p WHERE p.product.id = :productId")
    List<Purchase> findByProductId(@Param("productId") Long productId);
}
