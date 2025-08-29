package com.example.eventplanner.repositories.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceProductRepository extends JpaRepository<ServiceProduct, Long> {
    @Modifying
    @Query("UPDATE ServiceProduct e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);

    @Query(value = """
    SELECT sp.*
    FROM serviceproduct sp
    LEFT JOIN serviceproductreview spr
      ON sp.id = spr.serviceproduct_id AND spr.reviewstatus = 1
    WHERE sp.visible = true
    GROUP BY sp.id
    ORDER BY COALESCE(AVG(spr.grade), 0) DESC
    LIMIT 5
    """, nativeQuery = true)
    List<ServiceProduct> findTop5();

    @Query("SELECT sp FROM ServiceProduct sp " +
            "WHERE (:name LIKE '' OR LOWER(sp.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description LIKE '' OR LOWER(sp.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:categoryIds IS NULL OR sp.category.id in :categoryIds) " +
            "AND (:available IS NULL OR sp.available = :available) " +
            "AND (sp.visible = true) " +
            "AND (:minPrice IS NULL OR sp.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR sp.price <= :maxPrice) " +
            "AND (:typeIds IS NULL OR EXISTS (" +
            "   SELECT 1 " +
            "   FROM sp.availableEventTypes type " +
            "   WHERE type.id in :typeIds )) " +
            "AND (:spp IS NULL OR sp.serviceProductProvider.id = :spp) " +
            "AND (:type IS NULL OR TYPE(sp) = :type) " +
            "AND (TYPE(sp) != Service OR (" +
            "       (:minDuration IS NULL OR TREAT(sp AS Service).duration >= :minDuration) " +
            "   AND (:maxDuration IS NULL OR TREAT(sp AS Service).duration <= :maxDuration) " +
            "   AND (:automaticReserved IS NULL OR TREAT(sp AS Service).automaticReserved = :automaticReserved))) "
    )
    Page<ServiceProduct> findAllFiltered(
            @Param("type") Class<?> type,
            @Param("name") String name,
            @Param("description") String description,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("available") Boolean available,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("typeIds") List<Long> availableEventTypeIds,
            @Param("spp") Long serviceProductProviderId,
            @Param("minDuration") Float minDuration,
            @Param("maxDuration") Float maxDuration,
            @Param("automaticReserved") Boolean automaticReserved,
            Pageable pageable
    );

    @Query("SELECT MIN(sp.price), MAX(sp.price) FROM ServiceProduct sp WHERE sp.visible = true")
    List<Object[]> findPriceRange();
    @Query("SELECT MIN(s.duration), MAX(s.duration) FROM Service s WHERE s.visible = true")
    List<Object[]> findDurationRange();

    @Query("SELECT new com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto(sp.id, sp.name, sp.price, sp.discount) " +
            "FROM ServiceProduct sp WHERE sp.serviceProductProvider.id = :sppId")
    List<PriceListDto> getPriceListBySppId(@Param("sppId") Long sppId);
}
