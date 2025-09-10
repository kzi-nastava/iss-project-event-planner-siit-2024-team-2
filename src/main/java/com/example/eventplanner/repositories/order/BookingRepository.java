package com.example.eventplanner.repositories.order;

import com.example.eventplanner.dto.order.booking.BookingReminderDto;
import com.example.eventplanner.model.order.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
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

    @Query(value = """
        SELECT
            b.id AS bookingId,
            e.eventorganizer_id AS organizerId,
            e.name AS eventName,
            s.name AS serviceName,
            b.date AS startTime
        FROM booking b
        JOIN serviceproduct s ON b.service_id = s.id
        JOIN budget_booking bb ON b.id = bb.bookings_id
        JOIN event_budget eb ON bb.budget_id = eb.budgets_id
        JOIN event e ON e.id = eb.event_id
        WHERE b.date BETWEEN :now AND :in65Minutes
            AND b.sentReminder = false
            AND b.status = 0
            AND b.active = true
            AND e.active = true
            AND s.active = true
    """, nativeQuery = true)
    List<BookingReminderDto> findBookingsStartingInOneHour(
            @Param("now") Instant now,
            @Param("in65Minutes") Instant in65Minutes);

    @Modifying
    @Query("UPDATE Booking b SET b.sentReminder = true WHERE b.id IN :bookingIds")
    void updateSentReminders(List<Long> bookingIds);
}
