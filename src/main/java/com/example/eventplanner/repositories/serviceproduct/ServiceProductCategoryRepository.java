package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProductCategoryRepository extends JpaRepository<ServiceProductCategory, Long> {
    List<EventType> findAllEventTypes();
}
