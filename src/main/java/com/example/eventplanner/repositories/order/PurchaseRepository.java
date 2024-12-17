package com.example.eventplanner.repositories.order;

import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Modifying
    @Query("UPDATE Purchase e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
}
