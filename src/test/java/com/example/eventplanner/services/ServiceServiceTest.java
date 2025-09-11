package com.example.eventplanner.services;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.TestBookingReminderDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.order.booking.BookingReminderDto;
import com.example.eventplanner.dto.order.booking.PendingBookingDto;
import com.example.eventplanner.dto.util.DateRangeDto;
import com.example.eventplanner.dto.util.EmailDetails;
import com.example.eventplanner.exception.ConflictException;
import com.example.eventplanner.exception.ForbiddenException;
import com.example.eventplanner.exception.NotFoundException;
import com.example.eventplanner.exception.UnauthorizedException;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.BookingStatus;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.order.BookingRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import com.example.eventplanner.services.communication.NotificationService;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import com.example.eventplanner.services.util.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {

    @Mock
    EventRepository eventRepository;
    @Mock
    ServiceRepository serviceRepository;
    @Mock
    AuthUtil authUtil;
    @Mock
    BookingService bookingService;

    @InjectMocks
    ServiceService serviceService;

    EventOrganizer eventOrganizer;
    Service service;
    Event event;

    final static long serviceId = 10L;
    final static long providerId = 11L;
    final static long organizerId = 12L;
    final static long eventId = 13L;

    @BeforeEach
    void setUp() {
        ServiceProductProvider serviceProductProvider = new ServiceProductProvider();
        serviceProductProvider.setId(providerId);
        serviceProductProvider.setEmail("provider@example.com");

        service = new Service();
        service.setId(serviceId);
        service.setName("Test Service");
        service.setPrice(100.0);
        service.setDiscount(10.0);
        service.setAvailable(true);
        service.setVisible(true);
        service.setDuration(1);
        service.setMinEngagementDuration(0);
        service.setMaxEngagementDuration(0);
        service.setReservationDaysDeadline(0);
        service.setServiceProductProvider(serviceProductProvider);
        service.setImages(new ArrayList<>());
        service.setAvailableEventTypes(new ArrayList<>());

        eventOrganizer = new EventOrganizer();
        eventOrganizer.setId(organizerId);
        eventOrganizer.setEmail("organizer@example.com");

        event = new Event();
        event.setId(eventId);
        event.setDate(new Date(System.currentTimeMillis() + 10 * BookingService.DAY_MS));
        event.setEventOrganizer(eventOrganizer);
        event.setName("Test Event");
        event.setDescription("Test Description");
    }

    @Test
    void getAvailableDates_ShouldReturnAvailableDates_WhenSuccessful() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(authUtil.getAuthenticatedUserId()).thenReturn(organizerId);

        List<DateRangeDto> expected = List.of(new DateRangeDto(1000L, 2000L));
        when(bookingService.getAvailableDates(service, event)).thenReturn(expected);

        List<DateRangeDto> result = serviceService.getAvailableDates(serviceId, eventId);
        assertEquals(expected, result);
        verify(bookingService).checkServiceAcceptingBookings(service);
        verify(bookingService).getAvailableDates(service, event);
    }

    @Test
    void getAvailableDates_ShouldThrowForbiddenException_WhenUserIsNotEventOwner() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        when(authUtil.getAuthenticatedUserId()).thenReturn(1000L);

        assertThrows(ForbiddenException.class, () -> serviceService.getAvailableDates(serviceId, eventId));
    }

    @Test
    void getAvailableDates_ShouldThrowNotFoundException_WhenServiceNotFound() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(authUtil.getAuthenticatedUserId()).thenReturn(organizerId);

        assertThrows(NotFoundException.class, () -> serviceService.getAvailableDates(serviceId, eventId));
    }

    @Test
    void getAvailableDates_ShouldThrowNotFoundException_WhenEventNotFound() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        when(authUtil.getAuthenticatedUserId()).thenReturn(organizerId);

        assertThrows(NotFoundException.class, () -> serviceService.getAvailableDates(serviceId, eventId));
    }
}
