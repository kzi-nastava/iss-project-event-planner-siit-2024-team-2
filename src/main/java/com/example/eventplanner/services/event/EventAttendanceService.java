package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.AttendanceResult;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventAttendanceService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public AttendanceResult attendEvent(long id, BaseUser user) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return AttendanceResult.NOT_FOUND;

        if (event.getAttendees().contains(user))
            return AttendanceResult.SUCCESS;

        if (event.getAttendees().size() >= event.getMaxAttendances())
            return AttendanceResult.FULL;

        event.getAttendees().add(user);
        user.getAttendingEvents().add(event);

        eventRepository.save(event);
        userRepository.save(user);

        return AttendanceResult.SUCCESS;
    }

    @Transactional
    public AttendanceResult removeEventAttendance(long id, BaseUser user) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return AttendanceResult.NOT_FOUND;

        boolean wasAttending = event.getAttendees().remove(user);
        if (!wasAttending) // No need to save as the user wasn't attending the event
            return AttendanceResult.SUCCESS;
        user.getAttendingEvents().remove(event);

        eventRepository.save(event);
        userRepository.save(user);

        return AttendanceResult.SUCCESS;
    }

    @Transactional
    public boolean isUserAttending(long id, BaseUser user) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return false;
        return event.getAttendees().contains(user);
    }

    public boolean eventFull(long eventId, String email) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return false;
        if (event.getAttendees().stream().anyMatch(attendee -> attendee.getEmail().equals(email)))
            return false; // User is already attending
        return event.getAttendees().size() >= event.getMaxAttendances();
    }

    @Transactional(readOnly = true)
    public List<Long> getAttendingEvents(BaseUser user) {
        if (user == null || user.getAttendingEvents() == null) {
            return List.of();
        }

        return user.getAttendingEvents()
                .stream()
                .map(Event::getId)
                .toList();
    }

}
