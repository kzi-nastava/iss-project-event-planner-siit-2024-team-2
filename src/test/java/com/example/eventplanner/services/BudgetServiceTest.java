package com.example.eventplanner.services;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.util.DateRangeDto;
import com.example.eventplanner.exception.ForbiddenException;
import com.example.eventplanner.exception.NotFoundException;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.repositories.event.BudgetRepository;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import com.example.eventplanner.services.event.BudgetService;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    BudgetRepository budgetRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    AuthUtil authUtil;
    @Mock
    BookingService bookingService;

    @InjectMocks
    BudgetService budgetService;

    Budget budget;
    EventOrganizer eventOrganizer;
    Event event;

    final static long budgetId = 11L;
    final static long organizerId = 12L;
    final static long eventId = 13L;

    @BeforeEach
    void setUp() {
        eventOrganizer = new EventOrganizer();
        eventOrganizer.setId(organizerId);
        eventOrganizer.setEmail("organizer@example.com");

        event = new Event();
        event.setId(eventId);
        event.setDate(new Date(System.currentTimeMillis() + 10 * BookingService.DAY_MS));
        event.setEventOrganizer(eventOrganizer);
        event.setName("Test Event");
        event.setDescription("Test Description");

        budget = new Budget();
        budget.setId(budgetId);
        budget.setPlannedSpending(100.0);
        budget.setCurrentSpent(0.0);
        budget.setBookings(new ArrayList<>());
    }

    @Test
    void addBookingToBudget_ShouldAddBookingToBudget_WhenSuccessful() {
        when(eventRepository.findByBudgetId(budgetId)).thenReturn(Optional.of(event));
        when(authUtil.getAuthenticatedUserId()).thenReturn(organizerId);
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        BookingNoIdDto bookingDto = new BookingNoIdDto();
        bookingDto.setPrice(50.0);
        Booking booking = new Booking();
        booking.setId(10L);
        booking.setPrice(50.0);
        when(bookingService.book(bookingDto, event)).thenReturn(booking);

        budgetService.addBookingToBudget(budgetId, bookingDto);
        verify(bookingService).book(bookingDto, event);
        verify(budgetRepository).save(budget);
        verify(bookingService).sendBookingEmails(booking, event);

        assertEquals(50.0, budget.getCurrentSpent());
        assertEquals(1, budget.getBookings().size());
    }

    @Test
    void addBookingToBudget_ShouldThrowForbiddenException_WhenUserIsNotEventOwner() {
        when(eventRepository.findByBudgetId(budgetId)).thenReturn(Optional.of(event));
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        when(authUtil.getAuthenticatedUserId()).thenReturn(999L);

        assertThrows(ForbiddenException.class, () -> budgetService.addBookingToBudget(budgetId, new BookingNoIdDto()));
    }

    @Test
    void addBookingToBudget_ShouldThrowNotFoundException_WhenEventNotFound() {
        when(eventRepository.findByBudgetId(budgetId)).thenReturn(Optional.empty());
        when(authUtil.getAuthenticatedUserId()).thenReturn(organizerId);
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        assertThrows(NotFoundException.class, () -> budgetService.addBookingToBudget(budgetId, new BookingNoIdDto()));
    }

    @Test
    void addBookingToBudget_ShouldThrowNotFoundException_WhenBudgetNotFound() {
        when(authUtil.getAuthenticatedUserId()).thenReturn(organizerId);
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.addBookingToBudget(budgetId, new BookingNoIdDto()));
    }
}
