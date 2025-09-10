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
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    ServiceRepository serviceRepository;
    @Mock
    AuthUtil authUtil;
    @Mock
    EventOrganizerRepository eventOrganizerRepository;
    @Mock
    EmailService emailService;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    BookingService bookingService;

    ServiceProductProvider serviceProductProvider;
    EventOrganizer eventOrganizer;
    Service service;
    Event event;

    final static long serviceId = 10L;
    final static long providerId = 11L;
    final static long organizerId = 12L;
    final static long eventId = 13L;
    final static long bookingId = 14L;

    @BeforeEach
    void setUp() {
        serviceProductProvider = new ServiceProductProvider();
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
    void book_ShouldSaveAndReturnBooking_WhenSuccessful() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        BookingNoIdDto dto = new BookingNoIdDto();
        dto.setServiceId(serviceId);
        dto.setDate(timeFromNowHours(1));
        dto.setDuration(1);

        BookingService spyService = Mockito.spy(bookingService);
        DateRangeDto availableRange = toDateRangeDto(dto.getDate(), 1);
        doReturn(List.of(availableRange)).when(spyService).getAvailableDates(eq(service), eq(event));

        Booking saved = toBooking(dto);
        when(bookingRepository.save(any(Booking.class))).thenReturn(saved);

        Booking result = spyService.book(dto, event);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void book_ShouldThrowNotFoundException_WhenServiceNotFound() {
        when(serviceRepository.findById(99L)).thenReturn(Optional.empty());
        BookingNoIdDto dto = new BookingNoIdDto();
        dto.setServiceId(99L);
        Event event = new Event();

        assertThrows(NotFoundException.class, () -> bookingService.book(dto, event));
    }

    @Test
    void book_ShouldThrowConflictException_WhenDurationMismatchWithFixedDurationService() {
        service.setDuration(2);
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        BookingNoIdDto dto = new BookingNoIdDto();
        dto.setServiceId(serviceId);
        dto.setDuration(1);
        dto.setDate(Instant.now());

        assertThrows(ConflictException.class, () -> bookingService.book(dto, new Event()));
    }

    @Test
    void book_ShouldThrowConflictException_WhenDurationMismatchWithDynamicDurationService() {
        service.setDuration(0);
        service.setMinEngagementDuration(2);
        service.setMaxEngagementDuration(4);
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        BookingNoIdDto dto = new BookingNoIdDto();
        dto.setServiceId(serviceId);
        dto.setDate(Instant.now());

        dto.setDuration(1);
        assertThrows(ConflictException.class, () -> bookingService.book(dto, new Event()));

        dto.setDuration(5);
        assertThrows(ConflictException.class, () -> bookingService.book(dto, new Event()));
    }

    @Test
    void book_ShouldThrowConflictException_WhenPeriodIsUnavailable() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        BookingNoIdDto dto = new BookingNoIdDto();
        dto.setServiceId(serviceId);
        dto.setDate(timeFromNowHours(1));
        dto.setDuration(1L);

        BookingService spyService = Mockito.spy(bookingService);

        DateRangeDto notMatchingRange = toDateRangeDto(addHours(dto.getDate(), 24), 1);
        doReturn(List.of(notMatchingRange)).when(spyService).getAvailableDates(eq(service), any(Event.class));

        assertThrows(ConflictException.class, () -> spyService.book(dto, new Event()));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void book_ShouldThrowConflictException_WhenAfterEventStart() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(bookingRepository.findByServiceId(serviceId)).thenReturn(new ArrayList<>());

        BookingNoIdDto dto = new BookingNoIdDto();
        dto.setServiceId(serviceId);
        dto.setDate(timeFromNowHours(15*24));
        dto.setDuration(1L);

        assertThrows(ConflictException.class, () -> bookingService.book(dto, event));
    }

    @Test
    void book_ShouldThrowConflictException_WhenBeforeCancellationDeadline() {
        service.setReservationDaysDeadline(1);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(bookingRepository.findByServiceId(serviceId)).thenReturn(new ArrayList<>());

        BookingNoIdDto dto = new BookingNoIdDto();
        dto.setServiceId(serviceId);
        dto.setDate(timeFromNowHours(12));
        dto.setDuration(1L);

        assertThrows(ConflictException.class, () -> bookingService.book(dto, event));
    }

    @Test
    void isAvailable_ShouldReturnTrue_WhenRangeCoversStartAndEnd() {
        long start = timeFromNowHours(2).toEpochMilli();
        long end = start + BookingService.HOUR_MS;
        BookingService spyService = Mockito.spy(bookingService);
        DateRangeDto range = new DateRangeDto(start - 1000, end + 1000);
        doReturn(List.of(range)).when(spyService).getAvailableDates(eq(service), eq(event));

        boolean available = spyService.isAvailable(service, event, start, end);
        assertTrue(available);
    }

    @Test
    void isAvailable_ShouldReturnFalse_WhenNotWithinRange() {
        long start = timeFromNowHours(2).toEpochMilli();
        long end = start + 2 * BookingService.HOUR_MS;
        BookingService spyService = Mockito.spy(bookingService);
        DateRangeDto startOverlap = new DateRangeDto(start - BookingService.HOUR_MS, end - BookingService.HOUR_MS);
        DateRangeDto endOverlap = new DateRangeDto(start + BookingService.HOUR_MS, end + BookingService.HOUR_MS);
        doReturn(List.of(startOverlap, endOverlap)).when(spyService).getAvailableDates(eq(service), eq(event));

        boolean available = spyService.isAvailable(service, event, start - 1000, end + 1000);
        assertFalse(available);
    }

    @Test
    void getAvailableDates_usesBookedAndConvertToAvailable() {
        Instant start = Instant.now();

        service.setDuration(2);
        event.setDate(Date.from(start));

        List<Booking> bookings = List.of(
                toAcceptedBooking(addHours(start, 1), 2), // 01:00 - 03:00
                toAcceptedBooking(addHours(start, 2), 2), // 02:00 - 04:00
                toAcceptedBooking(addHours(start, 9), 5)  // 09:00 - 14:00
        );

        when(bookingRepository.findByServiceId(serviceId)).thenReturn(bookings);

        List<DateRangeDto> expected = List.of(
                toDateRangeDto(addHours(start, 4), 5), // 04:00 - 09:00
                toDateRangeDto(addHours(start, 14), 10) // 14:00 - 24:00
        );

        List<DateRangeDto> result = bookingService.getAvailableDates(service, event);
        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @Test
    void checkServiceAcceptingBookings_throwsWhenNotAvailableOrNotVisible() {
        service.setAvailable(false);
        service.setVisible(true);

        assertThrows(ForbiddenException.class, () -> bookingService.checkServiceAcceptingBookings(service));

        service.setAvailable(true);
        service.setVisible(false);
        assertThrows(ForbiddenException.class, () -> bookingService.checkServiceAcceptingBookings(service));
    }

    @Test
    void checkServiceAcceptingBookings_ShouldThrowException_WhenProviderDeleted() {
        service.setServiceProductProvider(null);
        assertThrows(ForbiddenException.class, () -> bookingService.checkServiceAcceptingBookings(service));
    }

    @Test
    void accept_ShouldThrowUnauthorizedException_WhenUnauthorized() {
        when(authUtil.getAuthenticatedUser()).thenReturn(null);
        assertThrows(UnauthorizedException.class, () -> bookingService.accept(1L));
    }

    @Test
    void accept_ShouldThrowNotFoundException_WhenBookingNotFound() {
        when(authUtil.getAuthenticatedUser()).thenReturn(serviceProductProvider);
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.accept(99L));
    }

    @Test
    void accept_ShouldThrowForbiddenException_WhenNotServiceOwner() {
        BaseUser user = new BaseUser();
        user.setId(1000L);
        when(authUtil.getAuthenticatedUser()).thenReturn(user);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setService(service);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ForbiddenException.class, () -> bookingService.accept(bookingId));
    }

    @Test
    void accept_ShouldSaveBookingAndSendEmails_WhenSuccessful() {
        when(authUtil.getAuthenticatedUser()).thenReturn(serviceProductProvider);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setService(service);
        booking.setStatus(BookingStatus.PENDING);
        booking.setDate(timeFromNowHours(1));
        booking.setPrice(service.getPrice());
        booking.setDuration(service.getDuration());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(eventRepository.findByBookingId(bookingId)).thenReturn(event);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        BookingDto dto = bookingService.accept(bookingId);

        assertNotNull(dto);
        assertEquals(BookingStatus.ACCEPTED, booking.getStatus());
        verify(emailService, atLeastOnce()).sendMimeMessage(any(EmailDetails.class));
        verify(bookingRepository).save(booking);
    }

    @Test
    void getMyBookings_unauthorized_throwsUnauthorized() {
        when(authUtil.getAuthenticatedUser()).thenReturn(null);
        assertThrows(UnauthorizedException.class, () -> bookingService.getMyBookings(Pageable.unpaged()));
    }

    @Test
    void getMyBookings_ShouldReturnPendingDtos_WhenSuccessful() {
        when(authUtil.getAuthenticatedUser()).thenReturn(serviceProductProvider);

        Booking b1 = new Booking();
        b1.setId(1L);
        b1.setStatus(BookingStatus.PENDING);
        b1.setDate(Instant.now());
        Booking b2 = new Booking();
        b2.setId(2L);
        b2.setStatus(BookingStatus.PENDING);
        b2.setDate(Instant.now());

        List<Booking> bookings = List.of(b1, b2);
        Page<Booking> page = new PageImpl<>(bookings);
        when(bookingRepository.findAllByServiceProviderIdPending(eq(providerId), any(Pageable.class))).thenReturn(page);

        when(eventOrganizerRepository.findByBookingId(1L)).thenReturn(eventOrganizer);
        when(eventOrganizerRepository.findByBookingId(2L)).thenReturn(eventOrganizer);

        Page<PendingBookingDto> result = bookingService.getMyBookings(PageRequest.of(0, 10));
        assertEquals(2, result.getContent().size());
    }

    @Test
    void sendBookingEmails_ShouldSendConfirmation_WhenAutomaticReserved() {
        doNothing().when(emailService).sendMimeMessage(any(EmailDetails.class));

        service.setAutomaticReserved(true);
        Booking booking = new Booking();
        booking.setService(service);
        booking.setDate(Instant.now());

        bookingService.sendBookingEmails(booking, event);
        verify(emailService, times(2)).sendMimeMessage(any(EmailDetails.class));
    }

    @Test
    void sendBookingEmails_ShouldSendEmail_WhenNotAutomaticAndEOPresent() {
        doNothing().when(emailService).sendMimeMessage(any(EmailDetails.class));

        event.setEventOrganizer(eventOrganizer);
        service.setAutomaticReserved(false);

        Booking booking = new Booking();
        booking.setService(service);
        booking.setDate(Instant.now());

        bookingService.sendBookingEmails(booking, event);
        verify(emailService).sendMimeMessage(any(EmailDetails.class));
    }

    @Test
    void sendConfirmationEmails_ShouldSendToProviderAndEOIfPresent() {
        doNothing().when(emailService).sendMimeMessage(any(EmailDetails.class));

        Booking booking = new Booking();
        booking.setService(service);
        booking.setDate(Instant.now());

        bookingService.sendConfirmationEmails(booking, event);
        verify(emailService, times(2)).sendMimeMessage(any(EmailDetails.class));
    }

    @Test
    void sendReminderNotifications_ShouldNothingHappen_WhenNoBookings() {
        when(bookingRepository.findBookingsStartingInOneHour()).thenReturn(Collections.emptyList());
        bookingService.sendReminderNotifications();
        verify(notificationService, never()).sendNotification(any());
        verify(bookingRepository, never()).updateSentReminders(anyList());
    }

    @Test
    void sendReminderNotifications_ShouldSendNotificationsAndUpdateSentReminders_WhenRemindersArePresent() {
        BookingReminderDto reminderDto = new TestBookingReminderDto(
                bookingId,
                "Test Service",
                "Test Event",
                organizerId,
                new Date()
        );

        when(bookingRepository.findBookingsStartingInOneHour()).thenReturn(List.of(reminderDto));
        doNothing().when(notificationService).sendNotification(any());

        bookingService.sendReminderNotifications();

        verify(notificationService).sendNotification(argThat(n -> !n.getTitle().isBlank()));
        verify(bookingRepository).updateSentReminders(argThat(list -> list.contains(bookingId)));
    }

    @Test
    void getBookingsByProvider_ShouldDelegateToRepository() {
        Booking b = new Booking();
        b.setId(bookingId);
        b.setDate(Instant.now());

        when(bookingRepository.findByProviderId(providerId)).thenReturn(List.of(b));

        Collection<BookingDto> dtos = bookingService.getBookingsByProvider(providerId);
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
    }

    @Test
    void delete_ShouldThrowUnauthorizedException_WhenUnauthorized() {
        when(authUtil.getAuthenticatedUser()).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> bookingService.delete(bookingId));
    }

    @Test
    void delete_ShouldThrowForbiddenException_WhenNotServiceOwner() {
        Booking booking = new Booking();
        booking.setService(service);

        BaseUser user = new BaseUser();
        user.setId(1000L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(authUtil.getAuthenticatedUser()).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> bookingService.delete(bookingId));
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenNotFound() {
        when(authUtil.getAuthenticatedUser()).thenReturn(serviceProductProvider);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.delete(bookingId));
    }

    @Test
    void delete_ShouldDelegateToRepository() {
        Booking booking = new Booking();
        booking.setService(service);
        booking.setId(bookingId);
        when(authUtil.getAuthenticatedUser()).thenReturn(serviceProductProvider);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        boolean result = bookingService.delete(bookingId);

        assertTrue(result);
    }

    private static Instant timeFromNowHours(int hours) {
        return addHours(Instant.now(), hours);
    }
    private static Instant addHours(Instant instant, int hours) {
        return instant.plusMillis(hours * BookingService.HOUR_MS);
    }
    private static DateRangeDto toDateRangeDto(Instant start, long durationHours) {
        return new DateRangeDto(start.toEpochMilli(), start.toEpochMilli() + durationHours * BookingService.HOUR_MS);
    }
    private Booking toBooking(BookingNoIdDto dto) {
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setDate(dto.getDate());
        booking.setDuration(dto.getDuration());
        booking.setService(service);
        return booking;
    }
    private Booking toAcceptedBooking(Instant date, double duration) {
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setDate(date);
        booking.setDuration(duration);
        booking.setService(service);
        booking.setStatus(BookingStatus.ACCEPTED);
        return booking;
    }
}
