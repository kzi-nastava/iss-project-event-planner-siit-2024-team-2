package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.SPOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SPOrderRepository extends JpaRepository<SPOrder, Long> {
}
