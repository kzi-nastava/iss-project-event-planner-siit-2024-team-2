package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
}
