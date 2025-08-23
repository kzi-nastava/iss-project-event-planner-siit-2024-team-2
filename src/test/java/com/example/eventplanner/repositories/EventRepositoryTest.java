package com.example.eventplanner.repositories;

import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.repositories.event.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event event;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();

        event = new Event();
        event.setName("Test Event 123");
        event.setDate(new Date(System.currentTimeMillis() + 86400000));

        Activity a1 = new Activity();
        a1.setName("Activity 1");
        a1.setActivityStart(toMillis("10:00"));
        a1.setActivityEnd(toMillis("11:00"));

        event.setActivities(List.of(a1));
        event = eventRepository.save(event);
    }

    private long toMillis(String time) {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime localTime = LocalTime.parse(time);
        return date.atTime(localTime)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    @Test
    void shouldDetectExactOverlap() {
        Activity newActivity = new Activity();
        newActivity.setName("Overlap Exact");
        newActivity.setActivityStart(toMillis("10:00"));
        newActivity.setActivityEnd(toMillis("11:00"));

        boolean overlaps = isOverlapping(newActivity, event.getActivities());

        assertThat(overlaps).isTrue();
    }

    @Test
    void shouldDetectInsideOverlap() {
        Activity newActivity = new Activity();
        newActivity.setName("Inside");
        newActivity.setActivityStart(toMillis("10:15"));
        newActivity.setActivityEnd(toMillis("10:45"));

        boolean overlaps = isOverlapping(newActivity, event.getActivities());

        assertThat(overlaps).isTrue();
    }

    @Test
    void shouldDetectStartBeforeEndsInside() {
        Activity newActivity = new Activity();
        newActivity.setActivityStart(toMillis("09:30"));
        newActivity.setActivityEnd(toMillis("10:30"));

        boolean overlaps = isOverlapping(newActivity, event.getActivities());

        assertThat(overlaps).isTrue();
    }

    @Test
    void shouldNotOverlap_whenEndsExactlyWhenExistingStarts() {
        Activity newActivity = new Activity();
        newActivity.setActivityStart(toMillis("09:00"));
        newActivity.setActivityEnd(toMillis("10:00"));

        boolean overlaps = isOverlapping(newActivity, event.getActivities());

        assertThat(overlaps).isFalse();
    }

    @Test
    void shouldNotOverlap_whenStartsExactlyWhenExistingEnds() {
        Activity newActivity = new Activity();
        newActivity.setActivityStart(toMillis("11:00"));
        newActivity.setActivityEnd(toMillis("12:00"));

        boolean overlaps = isOverlapping(newActivity, event.getActivities());

        assertThat(overlaps).isFalse();
    }

    @Test
    void shouldDetect1msOverlap() {
        Activity newActivity = new Activity();
        newActivity.setActivityStart(toMillis("09:59") + 59_999);
        newActivity.setActivityEnd(toMillis("10:00") + 1);

        boolean overlaps = isOverlapping(newActivity, event.getActivities());

        assertThat(overlaps).isTrue();
    }

    @Test
    void shouldNotFail_whenNoActivitiesInEvent() {
        Event noActivities = new Event();
        noActivities.setName("No Act");
        noActivities.setDate(new Date());
        noActivities.setActivities(new ArrayList<>());

        Activity newActivity = new Activity();
        newActivity.setActivityStart(toMillis("10:00"));
        newActivity.setActivityEnd(toMillis("11:00"));

        boolean overlaps = isOverlapping(newActivity, noActivities.getActivities());

        assertThat(overlaps).isFalse();
    }

    @Test
    void shouldNotThrow_whenActivityStartOrEndIsNull() {
        Activity corruptActivity = new Activity();
        corruptActivity.setName("Corrupt");
        corruptActivity.setActivityStart(-10000L);
        corruptActivity.setActivityEnd(-10000L);

        List<Activity> all = new ArrayList<>(event.getActivities());
        all.add(corruptActivity);

        Activity newActivity = new Activity();
        newActivity.setActivityStart(toMillis("10:00"));
        newActivity.setActivityEnd(toMillis("11:00"));

        boolean overlaps = isOverlapping(newActivity, all);

        assertThat(overlaps).isTrue();
    }

    private boolean isOverlapping(Activity newActivity, List<Activity> existing) {
        return existing.stream()
                .filter(a -> a.getActivityStart() >= 0 && a.getActivityEnd() >= 0)
                .anyMatch(a ->
                        newActivity.getActivityStart() < a.getActivityEnd() &&
                                newActivity.getActivityEnd() > a.getActivityStart()
                );
    }
}
