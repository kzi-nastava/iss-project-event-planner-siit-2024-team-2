package com.example.eventplanner.services.order;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.order.booking.*;
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
import com.example.eventplanner.model.utils.BookingStatus;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.order.BookingRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import com.example.eventplanner.services.communication.NotificationService;
import com.example.eventplanner.services.util.DateUtil;
import com.example.eventplanner.services.util.EmailFormatUtil;
import com.example.eventplanner.services.util.EmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final ServiceRepository serviceRepository;

    static final long HOUR_MS = 60 * 60 * 1000;
    static final long DAY_MS = 24 * HOUR_MS;
    private final AuthUtil authUtil;
    private final EventOrganizerRepository eventOrganizerRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public List<BookingDto> getAll() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    public BookingDto getById(long id) {
        return bookingRepository.findById(id)
                .map(BookingMapper::toDto)
                .orElse(null);
    }

    public BookingDto create(BookingNoIdDto dto) {
        Service service = serviceRepository.getReferenceById(dto.getServiceId());
        Booking booking = BookingMapper.toEntity(dto, service);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto update(BookingNoIdDto dto, long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null)
            return null;

        Service service = serviceRepository.getReferenceById(dto.getServiceId());

        booking.setService(service);
        booking.setPrice(dto.getPrice());
        booking.setDuration(dto.getDuration());
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public boolean delete(long id) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            throw new UnauthorizedException("User not found");
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Booking not found"));
        if (booking.getService().getServiceProductProvider().getId() != user.getId())
            throw new ForbiddenException("User is not authorized to delete this booking");
        bookingRepository.deleteById(id);
        return true;
    }

    public Collection<BookingDto> getBookingsByProvider(Long providerId) {
        List<Booking> bookings = bookingRepository.findByProviderId(providerId);
        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    public Booking book(BookingNoIdDto bookingDto, Event event) throws NotFoundException, ConflictException, ForbiddenException{
        Service service = serviceRepository.findById(bookingDto.getServiceId())
                                            .orElseThrow(() -> new NotFoundException("Service not found"));
        checkServiceAcceptingBookings(service);

        bookingDto.setPrice(Math.max(service.getPrice() - service.getDiscount(), 0));

        if (service.getDuration() > 0 && bookingDto.getDuration() != service.getDuration())
            throw new ConflictException("Booking duration must be equal to service duration");
        if (service.getMinEngagementDuration() > 0 &&
                (bookingDto.getDuration() < service.getMinEngagementDuration() ||
                        bookingDto.getDuration() > service.getMaxEngagementDuration()))
            throw new ConflictException("Booking duration must be between " +
                    service.getMinEngagementDuration() + " and " + service.getMaxEngagementDuration());

        long startDate = bookingDto.getDate().toEpochMilli();
        long endDate = startDate + (long)(HOUR_MS * bookingDto.getDuration());
        if (!isAvailable(service, event, startDate, endDate))
            throw new ConflictException("Booking period is not available");

        Booking booking = BookingMapper.toEntity(bookingDto, service);
        return bookingRepository.save(booking);
    }

    public boolean isAvailable(Service service, Event event, long startDate, long endDate) {
        List<DateRangeDto> availableDates = getAvailableDates(service, event);
        return availableDates.stream()
                .anyMatch(d -> d.getStart() <= startDate && d.getEnd() >= endDate);
    }

    public List<DateRangeDto> getAvailableDates(Service service, Event event) {
        Long startDate = new Date().getTime() + service.getReservationDaysDeadline() * DAY_MS;
        Long endDate = event.getDate().getTime() + DAY_MS;
        List<DateRangeDto> bookedDates = getBookedDates(service, startDate, endDate);
        return convertToAvailable(startDate, endDate, bookedDates);
    }

    private List<DateRangeDto> getBookedDates(Service service, Long startDate, Long endDate) {
        List<DateRangeDto> bookedDates = bookingRepository.findByServiceId(service.getId())
                        .stream()
                        .filter(b -> b.getStatus() == BookingStatus.ACCEPTED)
                        .map(b -> new DateRangeDto(
                                b.getDate().toEpochMilli(),
                                b.getDate().toEpochMilli() + (long)(HOUR_MS * b.getDuration())))
                        .sorted(Comparator.comparing(DateRangeDto::getStart))
                        .filter(bd -> bd.getEnd() >= startDate && bd.getStart() <= endDate)
                        .toList();
        float duration = service.getDuration() > 0
                ? service.getDuration()
                : service.getMinEngagementDuration();
        float durationMs = duration * HOUR_MS;
        return condenseDates(bookedDates, durationMs);
    }

    private List<DateRangeDto> condenseDates(List<DateRangeDto> dates, float durationMs) {
        List<DateRangeDto> condensed = new ArrayList<>();
        DateRangeDto last = null;
        for (DateRangeDto dateRange : dates) {
            if (condensed.isEmpty()) {
                condensed.add(dateRange);
                last = dateRange;
            } else {
                if (dateRange.getStart() <= last.getEnd()) // Overlapping
                    last.setEnd(Math.max(last.getEnd(), dateRange.getEnd()));
                else if (dateRange.getStart() - last.getEnd() >= durationMs) {
                    condensed.add(dateRange); // Usable range
                    last = dateRange;
                } else
                    last.setEnd(dateRange.getEnd()); // Not enough time for another booking
            }
        }
        return condensed;
    }

    /**
     * Converts booked dates to available dates.
     * Assumes that:
     * - bookedDates is sorted by start date
     * - all booking intervals intersect the [startDate, endDate] interval
     * - booking intervals don't overlap
     */
    private List<DateRangeDto> convertToAvailable(Long startDate, Long endDate, List<DateRangeDto> bookedDates) {
        List<DateRangeDto> available = new ArrayList<>();
        if (bookedDates.isEmpty()) {
            available.add(new DateRangeDto(startDate, endDate));
            return available;
        }

        if (bookedDates.get(0).getStart() > startDate)
            available.add(new DateRangeDto(startDate, bookedDates.get(0).getStart()));

        for (int i = 1; i < bookedDates.size(); i++) {
            long gapStart = bookedDates.get(i - 1).getEnd();
            long gapEnd = bookedDates.get(i).getStart();
            if (gapStart < gapEnd)
                available.add(new DateRangeDto(gapStart, gapEnd));
        }

        if (bookedDates.get(bookedDates.size() - 1).getEnd() < endDate)
            available.add(new DateRangeDto(bookedDates.get(bookedDates.size() - 1).getEnd(), endDate));

        return available;
    }

    public void checkServiceAcceptingBookings(Service service) throws NotFoundException, ForbiddenException {
        if (!service.isAvailable() || !service.isVisible())
            throw new ForbiddenException("Service is not available");
        if (service.getServiceProductProvider() == null)
            throw new ForbiddenException("Service provider is deleted");
    }

    public BookingDto accept(Long id) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            throw new UnauthorizedException("User not found");
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Booking not found"));
        if (booking.getService().getServiceProductProvider().getId() != user.getId())
            throw new ForbiddenException("User is not authorized to accept this booking");
        booking.setStatus(BookingStatus.ACCEPTED);

        Event event = eventRepository.findByBookingId(id);
        if (event == null)
            throw new NotFoundException("Event not found");

        sendConfirmationEmails(booking, event);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public Page<PendingBookingDto> getMyBookings(Pageable pageable) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            throw new UnauthorizedException("User not found");
        return bookingRepository.findAllByServiceProviderIdPending(user.getId(), pageable)
                .map(b -> {
                    EventOrganizer organizer = eventOrganizerRepository.findByBookingId(b.getId());
                    return BookingMapper.toPendingDto(b, organizer);
                });
    }

    public void sendBookingEmails(Booking booking, Event event) {
        if (booking.getService().isAutomaticReserved()) {
            sendConfirmationEmails(booking, event);
        } else {
            String body = EmailFormatUtil.formatEOBookingRequestEmail(EventMapper.toSummaryDto(event), booking);
            if (event.getEventOrganizer() != null)
                emailService.sendMimeMessage(new EmailDetails(
                        event.getEventOrganizer().getEmail(),
                        "Event Planner - Booking Request",
                        body).withHtml(true));
        }
    }

    public void sendConfirmationEmails(Booking booking, Event event) {
        String body = EmailFormatUtil.formatBookingConfirmationEmail(EventMapper.toSummaryDto(event), booking);
        if (booking.getService().getServiceProductProvider() != null)
            emailService.sendMimeMessage(new EmailDetails(
                    booking.getService().getServiceProductProvider().getEmail(),
                    "Event Planner - Booking Confirmation",
                    body).withHtml(true));
        if (event.getEventOrganizer() != null)
            emailService.sendMimeMessage(new EmailDetails(
                    event.getEventOrganizer().getEmail(),
                    "Event Planner - Booking Confirmation",
                    body).withHtml(true));
    }

    @Transactional
    @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 1000 * 30) // Every 5 minutes, 30 seconds after startup
    public void sendReminderNotifications() {
        List<BookingReminderDto> bookings = bookingRepository.findBookingsStartingInOneHour();
        System.out.println(bookings.size());
        if (bookings.isEmpty())
            return;
        for (BookingReminderDto booking : bookings) {
            String formattedDate = DateUtil.formatDate(booking.getStartTime().getTime());
            String formattedTime = DateUtil.formatTime(booking.getStartTime().getTime());
            notificationService.sendNotification(new NotificationNoIdDto(
                    "Reminder: Your booking for **" + booking.getServiceName() + "** starts in 1 hour",
                    "The service *" + booking.getServiceName() + "* for your event *" + booking.getEventName() + "* is starting at " +
                            formattedDate + " " + formattedTime + " UTC.",
                    false,
                    false,
                    booking.getOrganizerId()
            ));
        }
        bookingRepository.updateSentReminders(bookings.stream().map(BookingReminderDto::getBookingId).toList());
    }
}
