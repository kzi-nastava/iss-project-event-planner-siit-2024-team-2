package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventReview;
import lombok.Getter;
import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.activity.ActivityMapper;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
@Setter
@NoArgsConstructor
public class EventService {
    Map<Long, Event> events = new HashMap<>();
    private long idCounter = 0;

    public List<EventDto> getAll() {
        return events.values()
                .stream()
                .filter(Entity::isActive)
                .map(EventMapper::toDto)
                .toList();
    }

    public EventDto getById(long id) {
        if (!events.containsKey(id) || !events.get(id).isActive())
            return null;
        return EventMapper.toDto(events.get(id));
    }

    public EventDto create(EventNoIdDto dto) {
        Event event = EventMapper.toEntity(dto);
        event.setId(idCounter++);
        event.setActive(true);
        // link type
        EventType tmpType = new EventType();
        tmpType.setId(event.getId());
        event.setType(tmpType);
        // link activities
        event.setActivities(new ArrayList<>());
        // link budgets
        event.setBudgets(new ArrayList<>());

        events.put(event.getId(), event);
        return EventMapper.toDto(event);
    }

    public EventDto update(EventNoIdDto dto, long id) {
        if (this.getById(id) == null)
            return null;
        Event event = EventMapper.toEntity(dto);

        // link type
        EventType tmpType = new EventType();
        tmpType.setId(dto.getTypeId());
        event.setType(tmpType);
        // link activities
        event.setActivities(new ArrayList<>());
        // link budgets
        event.setBudgets(new ArrayList<>());

        events.put(id, EventMapper.toEntity(dto));
        return EventMapper.toDto(event);
    }

    public boolean delete(long id) {
        if (this.getById(id) == null)
            return false;
        events.get(id).setActive(false);
        return true;
    }

    public List<Event> getTop5() {
        return events.values()
                .stream()
                .sorted(Comparator.comparing(Event::getDate))
                .limit(5)
                .toList();
    }

    public Collection<EventDto> getAllFilteredPaginated(
            int page, Integer size, String name, String description, String type,
            Integer minMaxAttendances, Integer maxMaxAttendances, Boolean open,
            List<Double> longitudes, List<Double> latitudes, Double maxDistance,
            Date startDate, Date endDate) {
        Stream<Event> filtered = events.values().stream()
                .filter(event -> name == null || name.isEmpty() || event.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(event -> description == null || description.isEmpty()
                        || event.getDescription().toLowerCase().contains(description.toLowerCase()))
                .filter(event -> type == null || type.isEmpty() || event.getType().getName().equals(type))
                .filter(event -> minMaxAttendances == null || event.getMaxAttendances() > maxMaxAttendances)
                .filter(event -> maxMaxAttendances == null || event.getMaxAttendances() < maxMaxAttendances)
                .filter(event -> open == null || event.isOpen() == open)
                .filter(event -> latitudes == null || longitudes == null || latitudes.size() != longitudes.size()
                        || isEventNearAnyCity(event, longitudes, latitudes, maxDistance))
                .filter(event -> startDate == null || event.getDate().after(startDate))
                .filter(event -> endDate == null || event.getDate().before(endDate));
        if (size != null)
            filtered = filtered.skip((long) page * size).limit(size);
        return filtered.map(EventMapper::toDto).toList();
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
        // Earth radius in kilometers
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // returns the distance in kilometers
    }

    public boolean createAgenda(long id, List<ActivityDto> activityDtos) {
        if (!events.containsKey(id) || !events.get(id).isActive())
            return false;
        Event event = events.get(id);
        List<Activity> activities = activityDtos.stream().map(ActivityMapper::toEntity).toList();
        event.setActivities(activities);
        return true;
    }
}
