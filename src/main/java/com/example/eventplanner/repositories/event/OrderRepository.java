package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
