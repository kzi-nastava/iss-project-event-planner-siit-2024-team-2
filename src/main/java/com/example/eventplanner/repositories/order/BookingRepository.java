package com.example.eventplanner.repositories.order;

import com.example.eventplanner.model.order.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying
    @Query("UPDATE Booking e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
}
