package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.order.PurchaseService;
import lombok.Getter;
import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.activity.ActivityMapper;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.order.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
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
        Event event = EventMapper.toEntity(dto);
        event.setActive(true);
        eventTypeRepository.findById(dto.getTypeId()).ifPresent(event::setType);
        event.setActivities(new ArrayList<>());
        event.setBudgets(new ArrayList<>());

        Event savedEvent = eventRepository.save(event);
        return EventMapper.toDto(savedEvent);
    }

    public EventDto update(EventNoIdDto dto, long id) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    Event event = EventMapper.toEntity(dto);
                    event.setId(id);
                    event.setActive(true);
                    event.setDate(dto.getDate());
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
        return eventRepository.findById(id)
                .map(event -> {
                    event.setActive(false);
                    eventRepository.save(event);
                    return true;
                })
                .orElse(false);
    }

    public Collection<EventSummaryDto> getTop5() {
        return eventRepository.findTop5ByOrderByDateAsc()
                .stream()
                .map(EventMapper::toSummaryDto)
                .toList();
    }

    public Collection<EventDto> getAllFilteredPaginated(
            int page, Integer size, String name, String description, String type,
            Integer minMaxAttendances, Integer maxMaxAttendances, Boolean open,
            List<Double> longitudes, List<Double> latitudes, Double maxDistance,
            Date startDate, Date endDate) {
        PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);
        return eventRepository.findAllFiltered(
                name, description, type, minMaxAttendances, maxMaxAttendances, open,
                //longitudes, latitudes, maxDistance,
                startDate, endDate, pageRequest
        ).stream().map(EventMapper::toDto).toList();
    }

    private static boolean isEventNearAnyCity(Event event, List<Double> longitudes, List<Double> latitudes, double maxDistance) {
        for (int i = 0; i < longitudes.size(); i++) {
            double cityLongitude = longitudes.get(i);
            double cityLatitude = latitudes.get(i);

            double distance = haversine(event.getLatitude(), event.getLongitude(), cityLatitude, cityLongitude);

            if (distance <= maxDistance) {
                return true;
            }
        }
        return false;
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

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
