package com.example.eventplanner.controllers;

import com.example.eventplanner.controllers.event.EventController;
import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.EventReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventRepository eventRepository;
    private Long eventId;

    @BeforeEach
    void setup() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDate(new Date(System.currentTimeMillis() + 86400000));
        eventId = event.getId();

        Activity existing = new Activity();
        existing.setName("Existing");
        existing.setActivityStart(time("10:00"));
        existing.setActivityEnd(time("11:00"));
        List<Activity> activityList = new ArrayList<>();
        activityList.add(existing);
        event.setActivities(activityList);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));
        eventId = 1L;
    }

    private long time(String hhmm) {
        LocalTime localTime = LocalTime.parse(hhmm);
        LocalDate date = LocalDate.now().plusDays(1);
        return date.atTime(localTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private ActivityDto dto(String name, String start, String end) {
        ActivityDto dto = new ActivityDto();
        dto.setName(name);
        dto.setActivityStart(time(start));
        dto.setActivityEnd(time(end));
        dto.setLocation("Test Room");
        dto.setDescription("Test Description");
        return dto;
    }
    @Test
    void createActivity_shouldSucceed_whenValidTime() throws Exception {
        ActivityDto dto = dto("Valid Activity", "11:00", "12:00");

        mockMvc.perform(post("/api/events/" + eventId + "/agenda/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Valid Activity"));
    }

    @Test
    void createActivity_shouldFail_whenOverlapStart() throws Exception {
        ActivityDto dto = dto("Overlap Start", "10:30", "11:30");

        mockMvc.perform(post("/api/events/" + eventId + "/agenda/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createActivity_shouldFail_whenInsideExisting() throws Exception {
        ActivityDto dto = dto("Inside", "10:15", "10:45");
        mockMvc.perform(post("/api/events/" + eventId + "/agenda/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createActivity_shouldFail_whenExactOverlap() throws Exception {
        ActivityDto dto = dto("Exact", "10:00", "11:00");

        mockMvc.perform(post("/api/events/" + eventId + "/agenda/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createActivity_shouldFail_whenEndBeforeStart() throws Exception {
        ActivityDto dto = dto("Reverse Time", "11:00", "10:00");

        mockMvc.perform(post("/api/events/" + eventId + "/agenda/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createActivity_shouldSucceed_whenTouchesEnd() throws Exception {
        ActivityDto dto = dto("Touches End", "11:00", "12:00");

        mockMvc.perform(post("/api/events/" + eventId + "/agenda/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}
