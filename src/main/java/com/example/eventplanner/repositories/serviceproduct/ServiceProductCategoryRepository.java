package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProductCategoryRepository extends JpaRepository<ServiceProductCategory, Long> {
}
