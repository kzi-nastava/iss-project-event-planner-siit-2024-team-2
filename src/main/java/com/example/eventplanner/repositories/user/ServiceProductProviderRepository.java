package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.event.EventCreatorProjection;
import com.example.eventplanner.model.serviceproduct.ServiceProductCreatorProjection;
import com.example.eventplanner.model.user.ServiceProductProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceProductProviderRepository extends JpaRepository<ServiceProductProvider, Long> {
    boolean existsByEmail(String email);

    @Query(value = "SELECT sp.id AS serviceProductId, u.name AS creatorUsername, u.email AS creatorEmail " +
            "FROM ServiceProduct sp " +
            "JOIN BaseUser u ON sp.creator_id = u.id " +
            "WHERE sp.id IN (:serviceProductIds)",
            nativeQuery = true)
    List<ServiceProductCreatorProjection> findCreatorsByServiceProductIds(
            @Param("serviceProductIds") List<Long> serviceProductIds);
}
