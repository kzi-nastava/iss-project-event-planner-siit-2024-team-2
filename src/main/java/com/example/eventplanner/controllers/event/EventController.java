package com.example.eventplanner.controllers.event;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.activity.ActivityIdDto;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.AttendanceResult;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.services.event.EventAttendanceService;
import com.example.eventplanner.services.event.EventReportService;
import com.example.eventplanner.services.event.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor()
public class EventController {
    private final EventService eventService;
    private final AuthUtil authUtil;
    private final EventReportService eventReportService;
    private final EventAttendanceService eventAttendanceService;

    @GetMapping
    public ResponseEntity<Page<EventDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) Integer minMaxAttendances,
            @RequestParam(required = false) Integer maxMaxAttendances,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<Double> latitudes,
            @RequestParam(required = false) List<Double> longitudes,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<EventDto> result = eventService.getAllFiltered(
                EventDto.class,
                page, size, sort, name, description, types, minMaxAttendances, maxMaxAttendances,
                open, latitudes, longitudes, maxDistance, startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<Page<EventDto>> getMyEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) Integer minMaxAttendances,
            @RequestParam(required = false) Integer maxMaxAttendances,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<Double> latitudes,
            @RequestParam(required = false) List<Double> longitudes,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        Sort sort = Sort.by(sortDirection, sortBy);
        EventOrganizer organizer = authUtil.getAuthenticatedEventOrganizer();
        if (organizer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Page<EventDto> result = eventService.getAllFilteredByOrganizer(
                EventDto.class, organizer.getId(),
                page, size, sort, name, description, types, minMaxAttendances, maxMaxAttendances,
                open, latitudes, longitudes, maxDistance, startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/summaries")
    public ResponseEntity<Page<EventSummaryDto>> getEventSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) Integer minMaxAttendances,
            @RequestParam(required = false) Integer maxMaxAttendances,
            @RequestParam(required = false) Boolean open,
            @RequestParam(required = false) List<Double> latitudes,
            @RequestParam(required = false) List<Double> longitudes,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null || user.getUserRole() != UserRole.ADMIN)
            open = true; // Users can only see open events
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<EventSummaryDto> result = eventService.getAllFiltered(
                EventSummaryDto.class,
                page, size, sort, name, description, types, minMaxAttendances, maxMaxAttendances,
                open, latitudes, longitudes, maxDistance, startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") Long id) {
        EventDto result = eventService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventNoIdDto dto) {
        EventOrganizer organizer = authUtil.getAuthenticatedEventOrganizer();
        if (organizer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        dto.setEventOrganizerId(organizer.getId());
        try {
            EventDto result = eventService.create(dto);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventDto> updateEvent(@Valid @PathVariable("id") Long id, @RequestBody EventNoIdDto dto) {
        EventDto result = eventService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EventDto> deleteEvent(@PathVariable("id") Long id) {
        boolean success = eventService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/top5")
    public ResponseEntity<Collection<EventSummaryDto>> getTop5() {
        Collection<EventSummaryDto> result = eventService.getTop5();
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getEventPdf(@PathVariable Long id) throws Exception {
        byte[] pdf = eventReportService.generateEventPdf (id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=event_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/{id}/agenda")
    public ResponseEntity<List<ActivityDto>> createAgenda(@PathVariable long id, @RequestBody List<ActivityDto> activities) {
        boolean success = eventService.createAgenda(id, activities);
        return success
                ? ResponseEntity.ok(activities)
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/agenda/activity")
    public ResponseEntity<ActivityDto> addActivity(@PathVariable long id, @RequestBody ActivityDto activity) {
        boolean success = eventService.addActivity(id, activity);
        return success
                ? ResponseEntity.ok(activity)
                : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/agenda/activity/{activityId}")
    public ResponseEntity<ActivityDto> updateActivity(@PathVariable long id, @PathVariable long activityId, @RequestBody ActivityDto activity) {
        boolean success = eventService.updateActivity(id, activityId, activity);
        return success
                ? ResponseEntity.ok(activity)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}/agenda/activity/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable long id, @PathVariable long activityId) {
        boolean success = eventService.deleteActivity(id, activityId);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/agenda")
    public ResponseEntity<List<ActivityIdDto>> getAgenda(@PathVariable long id) {
        List<ActivityIdDto> activities = eventService.getAgenda(id);
        return activities != null
                ? ResponseEntity.ok(activities)
                : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}/purchases")
    public ResponseEntity<List<PurchaseDto>> getPurchases(@PathVariable("id") Long id) {
        List<PurchaseDto> result = eventService.getPurchases(id);
        return result != null ?
            new ResponseEntity<>(result, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getBookings(@PathVariable("id") Long id) {
        List<BookingDto> result = eventService.getBookings(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/max-attendances-range")
    public ResponseEntity<List<Integer>> getMaxAttendancesRange() {
        List<Integer> result = eventService.getMaxAttendancesRange();
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/attend")
    public ResponseEntity<String> attendEvent(@PathVariable long id) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        AttendanceResult result = eventAttendanceService.attendEvent(id, user);

        if (result == AttendanceResult.FULL)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event is full");
        else if (result == AttendanceResult.NOT_FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        else
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/attend")
    public ResponseEntity<String> removeEventAttendance(@PathVariable long id) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        AttendanceResult result = eventAttendanceService.removeEventAttendance(id, user);

        if (result == AttendanceResult.NOT_FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        else
            return ResponseEntity.ok().build();
    }
}
