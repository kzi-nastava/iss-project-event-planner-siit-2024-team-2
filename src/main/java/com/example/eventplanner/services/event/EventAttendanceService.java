package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.AttendanceResult;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
