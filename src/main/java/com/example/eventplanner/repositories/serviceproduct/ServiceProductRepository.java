package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProductRepository extends JpaRepository<ServiceProduct, Long> {
}
