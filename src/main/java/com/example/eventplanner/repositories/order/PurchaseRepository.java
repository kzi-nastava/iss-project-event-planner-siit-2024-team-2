package com.example.eventplanner.repositories.order;

import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
