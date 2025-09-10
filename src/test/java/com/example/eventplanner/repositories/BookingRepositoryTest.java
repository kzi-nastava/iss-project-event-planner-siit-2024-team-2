package com.example.eventplanner.repositories;

import com.example.eventplanner.dto.order.booking.BookingReminderDto;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.BookingStatus;
import com.example.eventplanner.repositories.order.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager em;

    private ServiceProductProvider provider;
    private Service service;
    private Booking booking;
    private Budget budget;
    private Event event;

    @BeforeEach
    void setUp() {
        provider = new ServiceProductProvider();
        provider.setEmail("provider@test.com");
        provider = em.persist(provider);

        service = new Service();
        service.setName("Test Service");
        service.setServiceProductProvider(provider);
        service = em.persist(service);

        booking = new Booking();
        booking.setService(service);
        booking.setDate(Instant.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setActive(true);
        booking.setSentReminder(false);
        booking = em.persist(booking);

        budget = new Budget();
        budget.setBookings(List.of(booking));
        budget = em.persist(budget);

        event = new Event();
        event.setName("Test Event");
        event.setBudgets(List.of(budget));
        event = em.persist(event);

        em.flush();
    }

    @Test
    void deleteById_ShouldSetActiveFalse() {
        bookingRepository.deleteById(booking.getId());
        em.flush();
        em.clear();
        assertNull(em.find(Booking.class, booking.getId()));
    }

    @Test
    void findByProviderId_ShouldReturnBookings() {
        List<Booking> result = bookingRepository.findByProviderId(provider.getId());
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void findByServiceId_ShouldReturnBookings() {
        List<Booking> result = bookingRepository.findByServiceId(service.getId());
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void findAllByServiceProviderIdPending_ShouldReturnPendingBookings() {
        Booking acceptedBooking = new Booking();
        acceptedBooking.setService(service);
        acceptedBooking.setDate(Instant.now());
        acceptedBooking.setStatus(BookingStatus.ACCEPTED);
        acceptedBooking.setActive(true);
        acceptedBooking.setSentReminder(false);
        em.persistAndFlush(acceptedBooking);

        Page<Booking> result = bookingRepository.findAllByServiceProviderIdPending(provider.getId(), PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements()); // shouldn't return the accepted booking
        assertEquals(BookingStatus.PENDING, result.getContent().get(0).getStatus());
    }

    @Test
    void updateSentReminders_ShouldSetSentReminderTrue() {
        bookingRepository.updateSentReminders(List.of(booking.getId()));
        em.flush();
        em.clear();
        Booking updated = em.find(Booking.class, booking.getId());
        assertTrue(updated.isSentReminder());
    }

    @Test
    void findBookingsStartingInOneHour_ShouldReturnDueBookings() {
        booking.setDate(Instant.now().plus(90, ChronoUnit.MINUTES)); // should not be returned
        booking.setStatus(BookingStatus.ACCEPTED);
        booking.setSentReminder(false);
        em.persistAndFlush(booking);
        booking.setDate(Instant.now().plus(30, ChronoUnit.MINUTES)); // within next 65 minutes
        booking.setStatus(BookingStatus.ACCEPTED);
        booking.setSentReminder(false);
        booking = em.persistAndFlush(booking);

        List<BookingReminderDto> reminders = bookingRepository.findBookingsStartingInOneHour(
                Instant.now(), Instant.now().plus(65, ChronoUnit.MINUTES));
        assertFalse(reminders.isEmpty());
        assertEquals(booking.getId(), reminders.get(0).getBookingId());
        assertEquals(service.getName(), reminders.get(0).getServiceName());
    }
}
