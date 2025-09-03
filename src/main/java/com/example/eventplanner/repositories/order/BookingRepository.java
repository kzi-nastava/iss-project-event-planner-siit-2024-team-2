package com.example.eventplanner.repositories.order;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.model.order.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying
    @Query("UPDATE Booking e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);

    @Query("SELECT b FROM Booking b WHERE b.service.serviceProductProvider.id = :providerId")
    List<Booking> findByProviderId(@Param("providerId") Long providerId);

    @Query("SELECT b FROM Booking b WHERE b.service.id = :serviceId")
    List<Booking> findByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT b FROM Booking b WHERE b.service.serviceProductProvider.id = :providerId AND b.status = 1")
    Page<Booking> findAllByServiceProviderIdPending(long providerId, Pageable pageable);
}
