package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface ServiceProductRepository extends JpaRepository<ServiceProduct, Long> {
    @Modifying
    @Query("UPDATE ServiceProduct e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
    @Query("select f.sp " +
            "from (" +
            "   select avg(spr.grade) as grade, spr.serviceProduct as sp" +
            "   from ServiceProductReview spr " +
            "   where spr.reviewStatus = 1 and sp.id = :id and sp.visible = true" +
            "   group by spr.serviceProduct" +
            ") f " +
            "order by f.grade desc " +
            "limit 5")
    List<ServiceProduct> findTop5();
    @Query("SELECT sp FROM ServiceProduct sp " +
            "WHERE (:name IS NULL OR LOWER(sp.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description IS NULL OR LOWER(sp.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:categoryIds IS EMPTY OR sp.category in :categoryIds) " +
            "AND (:available IS NULL OR sp.available = :available) " +
            "AND (:visible IS NULL OR sp.visible = :visible) " +
            "AND (:minPrice IS NULL OR sp.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR sp.price <= :maxPrice) " +
            "AND (:typeIds IS EMPTY OR EXISTS (" +
            "   SELECT 0 " +
            "   FROM sp.availableEventTypes type" +
            "   WHERE type.id in :typeIds ))" +
            "AND (:spp IS NULL OR sp.serviceProductProvider = :spp)"
    )
    Page<ServiceProduct> findAllFiltered(
            Sort sort,
            @Param("name") String name,
            @Param("description") String description,
            @Param("categoryIds") Long[] categoryIds,
            @Param("available") Boolean available,
            @Param("visible") Boolean visible,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("typeIds") Long[] availableEventTypeIds,
            @Param("spp") Long serviceProductProviderId,
            Pageable pageable
    );
}
