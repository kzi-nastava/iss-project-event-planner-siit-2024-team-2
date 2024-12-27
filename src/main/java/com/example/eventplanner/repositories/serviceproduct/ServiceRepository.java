package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.serviceproduct.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    @Query("SELECT s FROM Service s " +
            "WHERE s.active = true " +
            "AND (s.name LIKE %:name%)")
    Page<Service> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT s FROM Service s " +
            "WHERE s.active = true " +
            "AND (:categories IS NULL OR s.category.name IN :categories) " +
            "AND (:minPrice IS NULL OR :minPrice <= s.price)" +
            "AND (:maxPrice IS NULL OR :maxPrice >= s.price)" +
            "AND (:available IS NULL OR s.available = :available)")
    Page<Service> findAllFiltered(@Param("minPrice") Float minPrice,
                                  @Param("maxPrice") Float maxPrice,
                                  @Param("available") Boolean available,
                                  @Param("categories") List<String> categories,
                                  Pageable pageable);
}
