package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceProductCategoryRepository extends JpaRepository<ServiceProductCategory, Long> {
    @Query("SELECT c FROM ServiceProductCategory c WHERE c.name = :name")
    Optional<ServiceProductCategory> findByName(@Param("name") String name);
}
