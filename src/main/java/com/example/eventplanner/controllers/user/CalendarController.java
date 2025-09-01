package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.services.event.EventService;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.services.user.EventOrganizerService;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final AuthUtil authUtil;
    private final EventService eventService;
    private final BookingService bookingService;
    private final UserService userService;

    /**
     * All users: get events the user is attending.
     */
    @GetMapping("/attending")
    public ResponseEntity<Collection<EventDto>> getAttendingEvents() {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Collection<EventDto> result = userService.getAttendingEvents(user.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Event organizers: get events they organize.
     */
    @GetMapping("/organized")
    public ResponseEntity<Collection<EventDto>> getOrganizedEvents() {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (user.getUserRole() != UserRole.EVENT_ORGANIZER &&
                user.getUserRole() != UserRole.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Collection<EventDto> result = eventService.getEventsByOrganizer(user.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Service product providers: get bookings of their services.
     */
    @GetMapping("/bookings")
    public ResponseEntity<Collection<BookingDto>> getBookingsForProvider() {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (user.getUserRole() != UserRole.SERVICE_PRODUCT_PROVIDER &&
                user.getUserRole() != UserRole.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Collection<BookingDto> result = bookingService.getBookingsByProvider(user.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

