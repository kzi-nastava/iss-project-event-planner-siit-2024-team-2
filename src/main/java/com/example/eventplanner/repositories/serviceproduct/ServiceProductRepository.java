package com.example.eventplanner.repositories.serviceproduct;

import com.example.model.event.Event;
import com.example.model.serviceproduct.ServiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProductRepository extends JpaRepository<ServiceProduct, Integer> {
}
