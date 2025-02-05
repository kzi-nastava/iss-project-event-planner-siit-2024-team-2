package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventCreatorProjection;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.order.PurchaseService;
import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.activity.ActivityMapper;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventOrganizerRepository eventOrganizerRepository;
    private final PurchaseService purchaseService;
    private final BookingService bookingService;

    public List<EventDto> getAll() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::toDto)
                .toList();
    }

    public EventDto getById(long id) {
        return eventRepository.findById(id)
                .map(EventMapper::toDto)
                .orElse(null);
    }

    public EventDto create(EventNoIdDto dto) {
        EventType type = eventTypeRepository.getReferenceById(dto.getTypeId());
        Event event = EventMapper.toEntity(dto, type, new ArrayList<>(), new ArrayList<>());
        Event savedEvent = eventRepository.save(event);
        return EventMapper.toDto(savedEvent);
    }

    public EventDto update(EventNoIdDto dto, long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setId(id);
                    event.setActive(true);
                    event.setDate(new Date(dto.getDate()));
                    event.setDescription(dto.getDescription());
                    event.setName(dto.getName());
                    event.setOpen(dto.isOpen());
                    event.setLatitude(dto.getLatitude());
                    event.setLongitude(dto.getLongitude());
                    event.setMaxAttendances(dto.getMaxAttendances());
                    eventTypeRepository.findById(dto.getTypeId()).ifPresent(event::setType);
                    event.setActivities(new ArrayList<>());
                    event.setBudgets(new ArrayList<>());
                    Event updatedEvent = eventRepository.save(event);
                    return EventMapper.toDto(updatedEvent);
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        if (!eventRepository.existsById(id))
            return false;
        eventRepository.deleteById(id);
        return true;
    }

    public Collection<EventSummaryDto> getTop5() {
        List<Event> events = eventRepository.findTop5ByOrderByDateAsc();
        return constructEventSummaries(events);
    }

//    public Page<EventDto> getAllFilteredFull(
//            int page, Integer size, Sort sort, String name, String description, List<Long> types,
//            Integer minMaxAttendances, Integer maxMaxAttendances, Boolean open,
//            List<Double> latitudes, List<Double> longitudes, Double maxDistance,
//            Long startDate, Long endDate) {
//        return getAllFiltered(page, size, sort, name, description, types, minMaxAttendances,
//                maxMaxAttendances, open, latitudes, longitudes, maxDistance, startDate, endDate).map(EventMapper::toDto);
//    }


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
                latitudesArray, longitudesArray, maxDistance,
                startDateTime, endDateTime, pageRequest);
        if (clazz == EventDto.class)
            return events.map(EventMapper::toDto).map(clazz::cast);
        else
            return constructEventSummaries(events).map(clazz::cast);
    }

    private Page<EventSummaryDto> constructEventSummaries(Page<Event> events) {
        Map<Long, EventCreatorProjection> creatorMap = constructEventCreatorMap(events.toList());
        return events.map(event -> {
            EventCreatorProjection eventCreator = creatorMap.get(event.getId());
            return EventMapper.toSummaryDto(event, eventCreator.getCreatorUsername(), eventCreator.getCreatorEmail());
        });
    }

    private List<EventSummaryDto> constructEventSummaries(List<Event> events) {
        Map<Long, EventCreatorProjection> creatorMap = constructEventCreatorMap(events);
        return events.stream().map(event -> {
            EventCreatorProjection eventCreator = creatorMap.get(event.getId());
            return EventMapper.toSummaryDto(event, eventCreator.getCreatorUsername(), eventCreator.getCreatorEmail());
        }).toList();
    }

    private Map<Long, EventCreatorProjection> constructEventCreatorMap(Collection<Event> events) {
        List<Long> eventIds = events.stream().map(Event::getId).toList();
        List<EventCreatorProjection> eventCreators = eventOrganizerRepository.findCreatorsByEventIds(eventIds);
        return eventCreators.stream()
                .collect(Collectors.toMap(EventCreatorProjection::getEventId, Function.identity()));
    }

//    private static boolean isEventNearAnyCity(Event event, List<Double> longitudes, List<Double> latitudes, double maxDistance) {
//        for (int i = 0; i < longitudes.size(); i++) {
//            double cityLongitude = longitudes.get(i);
//            double cityLatitude = latitudes.get(i);
//
//            double distance = haversine(event.getLatitude(), event.getLongitude(), cityLatitude, cityLongitude);
//
//            if (distance <= maxDistance) {
//                return true;
//            }
//        }
//        return false;
//    }

//    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
//        final int R = 6371;
//        double latDistance = Math.toRadians(lat2 - lat1);
//        double lonDistance = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return R * c;
//    }

    public boolean createAgenda(long id, List<ActivityDto> activityDtos) {
        return eventRepository.findById(id)
                .map(event -> {
                    List<Activity> activities = activityDtos.stream()
                            .map(ActivityMapper::toEntity)
                            .toList();
                    event.setActivities(activities);
                    eventRepository.save(event);
                    return true;
                })
                .orElse(false);
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
}
