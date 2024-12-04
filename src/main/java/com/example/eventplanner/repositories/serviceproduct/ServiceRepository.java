package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAllByIsActiveTrue();
    Optional<Service> findByIdAndIsActiveTrue(long id);

    @Query("SELECT s FROM Service s " +
            "WHERE s.isActive = true " +
            "AND (s.name IS NULL OR s.name LIKE %:name%)")
    List<Service> searchByName(@Param("name") String name);
}
