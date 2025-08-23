package com.example.eventplanner.services;

import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import com.example.eventplanner.services.event.EventService;
import com.example.eventplanner.services.event.InvitationService;
import com.example.eventplanner.utils.StatusPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.BadAttributeValueExpException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvitationService invitationService;

    private EventNoIdDto eventDto;
    private EventType eventType;
    private EventOrganizer organizer;
    private Event event;

    @BeforeEach
    void setUp() {
        eventDto = new EventNoIdDto();
        eventDto.setName("Test");
        eventDto.setDescription("Description");
        eventDto.setMaxAttendances(100);
        eventDto.setLatitude(44.0);
        eventDto.setLongitude(20.0);
        eventDto.setDate(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000L));
        eventDto.setOpen(true);
        eventDto.setEventTypeId(1L);
        eventDto.setEventOrganizerId(2L);
        eventDto.setInvitationEmails(new ArrayList<>());

        eventType = new EventType();
        eventType.setId(1L);

        organizer = new EventOrganizer();
        organizer.setId(2L);

        event = new Event();
        event.setId(10L);
        event.setName("Test");
        event.setType(eventType);
        event.setDate(new Date());
        event.setActivities(new ArrayList<>());
        event.setBudgets(new ArrayList<>());
        event.setEventOrganizer(organizer);
        event.setOpen(true);
        event.setAttendees(new ArrayList<>());
    }

    @Test
    void create_ShouldReturnEventDto_WhenValidInput() throws Exception {
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.of(eventType));
        when(userRepository.findById(2L)).thenReturn(Optional.of(organizer));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        EventDto result;

        try {
            result = eventService.create(eventDto);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        assertNotNull(result);
        assertEquals("Test", result.getName());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void create_ShouldReturnNull_WhenInvalidInput() {

        EventDto result;
        assertThrows(Exception.class, () -> eventService.create(new EventNoIdDto()));
        try {
            result = eventService.create(new EventNoIdDto());
        } catch (Exception ignored) {
            result = null;
        }
        assertNull(result);
    }

    @Test
    void create_ShouldThrowException_WhenEventTypeNotFound() {
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> eventService.create(eventDto));
    }

    @Test
    void getById_ShouldReturnDto_WhenFound() {
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        EventDto result = eventService.getById(10L).getValue();

        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    void getById_ShouldReturnNull_WhenNotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(eventService.getById(99L).getValue());
    }

    @Test
    void delete_ShouldReturnTrue_WhenExists() {
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        boolean result = eventService.delete(10L);

        assertTrue(result);
        verify(eventRepository).deleteById(10L);
    }

    @Test
    void delete_ShouldReturnFalse_WhenNotExists() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = eventService.delete(99L);

        assertFalse(result);
        verify(eventRepository, never()).deleteById(anyLong());
    }

    @Test
    void addActivity_ShouldAdd_WhenValidAndNoOverlap() {
        ActivityDto dto = new ActivityDto();
        dto.setName("Act 1");
        dto.setActivityStart(10L);
        dto.setActivityEnd(20L);

        Event existingEvent = new Event();
        existingEvent.setActivities(new ArrayList<>());

        when(eventRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any())).thenReturn(existingEvent);

        boolean result = eventService.addActivity(1L, dto);

        assertTrue(result);
        verify(eventRepository).save(existingEvent);
    }

    @Test
    void addActivity_ShouldFail_WhenOverlappingTime() {
        ActivityDto dto = new ActivityDto();
        dto.setActivityStart(15L);
        dto.setActivityEnd(25L);
        dto.setName("Activity1");

        Activity existing = new Activity();
        existing.setActivityStart(10L);
        existing.setActivityEnd(20L);

        Event event = new Event();
        event.setActivities(List.of(existing));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        boolean result = eventService.addActivity(1L, dto);

        assertFalse(result);
    }

    @Test
    void updateActivity_ShouldReturnFalse_WhenEventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = eventService.updateActivity(1L, 1L, new ActivityDto());

        assertFalse(result);
    }

    @Test
    void updateActivity_ShouldReturnFalse_WhenActivityNotFound() {
        Event event = new Event();
        event.setActivities(new ArrayList<>());

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        boolean result = eventService.updateActivity(1L, 99L, new ActivityDto());

        assertFalse(result);
    }

    @Test
    void isTimeValid_ShouldRejectOverlappingTimes() {
        Activity a1 = new Activity();
        a1.setActivityStart(10);
        a1.setActivityEnd(20);

        List<Activity> activities = List.of(a1);

        boolean result = invokeIsTimeValid(activities, 15, 25, null);

        assertFalse(result);
    }

    @Test
    void isTimeValid_ShouldAllowNonOverlappingTimes() {
        Activity a1 = new Activity();
        a1.setActivityStart(10);
        a1.setActivityEnd(20);

        List<Activity> activities = List.of(a1);

        boolean result = invokeIsTimeValid(activities, 21, 30, null);

        assertTrue(result);
    }

    @Test
    void addActivity_ShouldFail_WhenEmptyName() {
        ActivityDto dto = new ActivityDto();
        dto.setActivityStart(10);
        dto.setActivityEnd(10);
        boolean result = eventService.addActivity(1L, dto);
        assertFalse(result, "Failed: Added activity with name");
    }

    @Test
    void addActivity_ShouldFail_WhenEmptyStart() {
        ActivityDto dto = new ActivityDto();
        dto.setName("Test");
        dto.setActivityEnd(10);
        boolean result = eventService.addActivity(1L, dto);
        assertFalse(result, "Failed: Added activity with empty timestamp");
    }

    @Test
    void addActivity_ShouldFail_WhenEmptyEnd() {
        ActivityDto dto = new ActivityDto();
        dto.setName("Test");
        dto.setActivityStart(10);
        boolean result = eventService.addActivity(1L, dto);
        assertFalse(result, "Failed: Added activity with empty timestamp");
    }

    @Test
    void addActivity_EdgeCases_TimeOverlap() {
        Activity existing = new Activity();
        existing.setActivityStart(10);
        existing.setActivityEnd(20);

        Event event = new Event();
        event.setActivities(new ArrayList<>(List.of(existing)));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        List<ActivityTestCase> testCases = List.of(
                new ActivityTestCase(6, 5, false, "Start after end"),
                new ActivityTestCase(5, 9, true, "Before"),
                new ActivityTestCase(21, 25, true, "After"),
                new ActivityTestCase(10, 20, false, "Exact overlap"),
                new ActivityTestCase(9, 21, false, "Full overlap"),
                new ActivityTestCase(5, 15, false, "Starts before, ends inside"),
                new ActivityTestCase(15, 25, false, "Starts inside, ends after"),
                new ActivityTestCase(15, 18, false, "Fully inside"),
                new ActivityTestCase(20, 20, false, "Zero length"),
                new ActivityTestCase(22, 21, false, "End before start"),
                new ActivityTestCase(25, 30, true, "Starts at existing end"),
                new ActivityTestCase(2, 5, true, "Ends at existing start")
        );

        for (ActivityTestCase testCase : testCases) {
            ActivityDto dto = new ActivityDto();
            dto.setActivityStart(testCase.start);
            dto.setActivityEnd(testCase.end);
            dto.setName("Activity");
            boolean result = eventService.addActivity(1L, dto);
            assertEquals(testCase.expected, result, "Failed: " + testCase.description);
        }
    }

    private static class ActivityTestCase {
        long start;
        long end;
        boolean expected;
        String description;

        ActivityTestCase(long start, long end, boolean expected, String description) {
            this.start = start;
            this.end = end;
            this.expected = expected;
            this.description = description;
        }
    }


    private boolean invokeIsTimeValid(List<Activity> activities, long start, long end, Long activityId) {
        try {
            var method = EventService.class.getDeclaredMethod("isTimeValid", List.class, long.class, long.class, Long.class);
            method.setAccessible(true);
            return (boolean) method.invoke(eventService, activities, start, end, activityId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
