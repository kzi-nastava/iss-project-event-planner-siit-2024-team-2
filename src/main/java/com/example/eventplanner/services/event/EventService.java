package com.example.eventplanner.services.event;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.activity.ActivityIdDto;
import com.example.eventplanner.dto.event.activity.ActivityMapper;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.exception.ForbiddenException;
import com.example.eventplanner.exception.NotFoundException;
import com.example.eventplanner.exception.UnauthorizedException;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.event.Invitation;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import com.example.eventplanner.services.communication.NotificationService;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.order.PurchaseService;
import com.example.eventplanner.services.user.UserService;
import com.example.eventplanner.services.util.DateUtil;
import com.example.eventplanner.utils.StatusPair;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.BadAttributeValueExpException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final PurchaseService purchaseService;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final InvitationService invitationService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final AuthUtil authUtil;

    public List<EventDto> getAll() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::toDto)
                .toList();
    }

    public StatusPair<EventDto> getById(long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return new StatusPair<>(null, HttpStatus.NOT_FOUND);
        if (!event.isOpen()) {
            BaseUser user = authUtil.getAuthenticatedUser();
            if (user == null)
                return new StatusPair<>(null, HttpStatus.UNAUTHORIZED);
            if (user.getUserRole() == UserRole.EVENT_ORGANIZER &&
                    event.getEventOrganizer().getId() == user.getId())
                return new StatusPair<>(EventMapper.toDto(event), HttpStatus.OK);
            if (user.getUserRole() != UserRole.ADMIN &&
                    event.getInvitations()
                            .stream()
                            .noneMatch(invitation -> invitation.isAccepted()
                                    && invitation.getEmail().equals(user.getEmail())))
                return new StatusPair<>(null, HttpStatus.FORBIDDEN);
        }
        return new StatusPair<>(EventMapper.toDto(event), HttpStatus.OK);
    }

    public EventDto create(EventNoIdDto dto) throws Exception {
        if (dto.getDate() == null || dto.getName().isEmpty()) throw new BadAttributeValueExpException("Date or name empty");
        EventType type = eventTypeRepository.findById(dto.getEventTypeId()).orElseThrow();
        EventOrganizer eventOrganizer = (EventOrganizer) userRepository.findById(dto.getEventOrganizerId()).orElseThrow();
        Event event = EventMapper.toEntity(dto, type, eventOrganizer);
        List<Invitation> invitations = dto.getInvitationEmails()
                .stream()
                .map(email -> new Invitation(event, email, !userService.existsByEmail(email)))
                .toList();
        invitationService.sendInvitations(invitations);
        event.setInvitations(invitations);
        return EventMapper.toDto(eventRepository.save(event));
    }

    @Transactional
    public EventDto update(EventNoIdDto dto, long id) {
        boolean admin = authUtil.isAdmin();
        Event event = getAuthorizedEvent(id);

        event.setId(id);
        event.setActive(true);
        event.setDate(dto.getDate());
        event.setDescription(dto.getDescription());
        event.setName(dto.getName());
        event.setOpen(dto.isOpen());
        event.setLatitude(dto.getLatitude());
        event.setLongitude(dto.getLongitude());
        event.setMaxAttendances(dto.getMaxAttendances());
        eventTypeRepository.findById(dto.getEventTypeId()).ifPresent(event::setType);
        if (admin) // Only admin can change event organizer
            userRepository.findById(dto.getEventOrganizerId()).ifPresent(eo -> event.setEventOrganizer((EventOrganizer) eo));
        Map<String, Invitation> existingInvitations = event.getInvitations()
                .stream()
                .collect(Collectors.toMap(Invitation::getEmail, invitation -> invitation));
        List<Invitation> newInvitations = dto.getInvitationEmails()
                .stream()
                .filter(email -> !existingInvitations.containsKey(email))
                .map(email -> new Invitation(event, email, !userService.existsByEmail(email)))
                .toList();
        invitationService.sendInvitations(newInvitations);
        event.getInvitations().addAll(newInvitations);
        Event updatedEvent = eventRepository.save(event);
        sendUpdateNotifications(updatedEvent);
        return EventMapper.toDto(updatedEvent);
    }

    public boolean delete(long id) {
        Event event = getAuthorizedEvent(id);
        sendEventNotifications(event, "Event deleted", "Event " + event.getName() + " has been deleted");
        event.getAttendees().forEach(attendee -> attendee.getAttendingEvents().remove(event));
        userRepository.saveAll(event.getAttendees());
        eventRepository.deleteById(id);
        return true;
    }

    public Collection<EventSummaryDto> getTop5() {
        return eventRepository.findTop5(LocalDateTime.now())
                .stream()
                .map(EventMapper::toSummaryDto)
                .toList();
    }

    public <T> Page<T> getAllFiltered(
            Class<T> clazz, int page, Integer size, Sort sort, String name, String description, List<Long> types,
            Integer minMaxAttendances, Integer maxMaxAttendances, Boolean open,
            List<Double> latitudes, List<Double> longitudes, Double maxDistance,
            Long startDate, Long endDate) {
        PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10, sort);
        LocalDateTime startDateTime = startDate != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), TimeZone.getDefault().toZoneId()) :
                LocalDateTime.of(-4711, 1, 1, 0, 0);
        LocalDateTime endDateTime = endDate != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), TimeZone.getDefault().toZoneId()) :
                LocalDateTime.of(294275, 12, 31, 23, 59);
        Double[] latitudesArray, longitudesArray;
        if (latitudes == null || longitudes == null || maxDistance == null  || maxDistance == 0) {
            latitudesArray = new Double[0];
            longitudesArray = new Double[0];
            maxDistance = 0D;
        } else {
            latitudesArray = latitudes.toArray(new Double[0]);
            longitudesArray = longitudes.toArray(new Double[0]);
        }
        Long[] eventTypeIdsArray = types == null ?
                new Long[0] :
                types.toArray(new Long[0]);
        Page<Event> events = eventRepository.findAllFiltered(
                name, description, eventTypeIdsArray, minMaxAttendances, maxMaxAttendances, open,
                latitudesArray, longitudesArray,
                maxDistance,
                startDateTime, endDateTime, pageRequest);
        if (clazz == EventDto.class)
            return events.map(EventMapper::toDto).map(clazz::cast);
        else
            return events.map(EventMapper::toSummaryDto)
                    .map(clazz::cast);
    }
    public <T> Page<T> getAllFilteredByOrganizer(
            Class<T> clazz, long organizerId, int page, Integer size, Sort sort, String name, String description, List<Long> types,
            Integer minMaxAttendances, Integer maxMaxAttendances, Boolean open,
            List<Double> latitudes, List<Double> longitudes, Double maxDistance,
            Long startDate, Long endDate) {
        PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10, sort);
        LocalDateTime startDateTime = startDate != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), TimeZone.getDefault().toZoneId()) :
                LocalDateTime.of(-4711, 1, 1, 0, 0);
        LocalDateTime endDateTime = endDate != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), TimeZone.getDefault().toZoneId()) :
                LocalDateTime.of(294275, 12, 31, 23, 59);
        Double[] latitudesArray, longitudesArray;
        if (latitudes == null || longitudes == null || maxDistance == null  || maxDistance == 0) {
            latitudesArray = new Double[0];
            longitudesArray = new Double[0];
            maxDistance = 0D;
        } else {
            latitudesArray = latitudes.toArray(new Double[0]);
            longitudesArray = longitudes.toArray(new Double[0]);
        }
        Long[] eventTypeIdsArray = types == null ?
                new Long[0] :
                types.toArray(new Long[0]);
        Page<Event> events = eventRepository.findAllFilteredByOrganizer(
                organizerId, name, description, eventTypeIdsArray, minMaxAttendances, maxMaxAttendances, open,
                latitudesArray, longitudesArray,
                maxDistance,
                startDateTime, endDateTime, pageRequest);
        if (clazz == EventDto.class)
            return events.map(EventMapper::toDto).map(clazz::cast);
        else
            return events.map(EventMapper::toSummaryDto)
                    .map(clazz::cast);
    }

    public boolean createAgenda(long id, List<ActivityDto> activityDtos) {
        Event event = getAuthorizedEvent(id);

        List<Activity> activities = activityDtos.stream()
                .map(ActivityMapper::toEntity)
                .toList();
        event.setActivities(activities);
        sendUpdateNotifications(event, "Event " + event.getName() + " had its agenda updated");
        eventRepository.save(event);
        return true;
    }

    public List<PurchaseDto> getPurchases(long id) {
        return purchaseService.getAll()
                .stream()
                .filter(purchase -> purchase.getEvent().getId() == id)
                .toList();
    }

    public List<BookingDto> getBookings(long id) {
        return bookingService.getAll()
                .stream()
                .filter(booking -> booking.getEvent().getId() == id)
                .toList();
    }

    public List<Integer> getMaxAttendancesRange() {
        List<Object[]> result = eventRepository.findMaxAttendancesRange();
        Integer min = (Integer) result.get(0)[0];
        Integer max = (Integer) result.get(0)[1];
        return Arrays.asList(min, max);
    }

    public boolean addActivity(long id, ActivityDto activity) {
        Event event = getAuthorizedEvent(id);
        if (activity.getName().isEmpty()) return false;
        if (!isTimeValid(event.getActivities(), activity.getActivityStart(), activity.getActivityEnd(), null)) return false;
        event.getActivities().add(ActivityMapper.toEntity(activity));
        sendUpdateNotifications(event, "Event " + event.getName() + " had its agenda updated");
        eventRepository.save(event);
        return true;
    }

    public List<ActivityIdDto> getAgenda(long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) return null;
        return event
                .getActivities()
                .stream()
                .filter(Entity::isActive)
                .map(ActivityMapper::toIdDto)
                .sorted(Comparator.comparing(ActivityIdDto::getActivityStart))
                .toList();
    }

    public boolean updateActivity(long eventId, long activityId, ActivityDto dto) {
        Event event = getAuthorizedEvent(eventId);

        Optional<Activity> optionalActivity = event.getActivities().stream()
                .filter(a -> a.getId() == activityId)
                .findFirst();

        if (optionalActivity.isEmpty()) return false;
        if (dto.getName().isEmpty()) return false;
        if (!isTimeValid(event.getActivities(), dto.getActivityStart(), dto.getActivityEnd(), activityId)) return false;

        Activity activity = optionalActivity.get();
        activity.setName(dto.getName());
        activity.setActivityStart(dto.getActivityStart());
        activity.setActivityEnd(dto.getActivityEnd());
        activity.setDescription(dto.getDescription());
        activity.setLocation(dto.getLocation());

        sendUpdateNotifications(event, "Event " + event.getName() + " had its agenda updated");

        eventRepository.save(event);
        return true;
    }

    public boolean deleteActivity(long eventId, long activityId) {
        Event event = getAuthorizedEvent(eventId);
        Activity activity = event.getActivities().stream().filter(a -> a.getId() == activityId).findFirst().orElse(null);
        if (activity != null) {
            activity.setActive(false);
        }
        sendUpdateNotifications(event, "Event " + event.getName() + " had its agenda updated");
        eventRepository.save(event);
        return activity != null;
    }

    private boolean isTimeValid(List<Activity> activities, long start, long end, Long activityId) {
        if (start >= end) return false;

        for (Activity activity : activities.stream().filter(Entity::isActive).toList()) {
            if (activityId != null && activity.getId() == activityId) continue;

            long existingStart = activity.getActivityStart();
            long existingEnd = activity.getActivityEnd();

            boolean overlaps = start < existingEnd && end > existingStart;

            if (overlaps) return false;
        }

        return true;
    }

    private void sendUpdateNotifications(Event event) {
        sendUpdateNotifications(event, "Event " + event.getName() + " has been updated");
    }
    private void sendUpdateNotifications(Event event, String message) {
        sendEventNotifications(event, "Event updated", message);
    }
    private void sendEventNotifications(Event event, String title, String message) {
        if (event.getAttendees() == null) return;
        event.getAttendees().forEach(attendee ->
                notificationService.sendNotification(new NotificationNoIdDto(
                        title,
                        message,
                        attendee.getId()
                )));
    }

    @NotNull
    private Event getAuthorizedEvent(long id) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            throw new UnauthorizedException("User not found");
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            throw new NotFoundException("Event not found");
        boolean admin = user.getUserRole() == UserRole.ADMIN;
        if (event.getEventOrganizer().getId() != user.getId() && !admin)
            throw new ForbiddenException("You are not authorized to access this event");
        return event;
    }
}
