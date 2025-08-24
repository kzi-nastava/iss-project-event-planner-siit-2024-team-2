package com.example.eventplanner.controllers;

import com.example.eventplanner.dto.event.event.EventNoIdDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Date;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private EventNoIdDto validEvent() {
        EventNoIdDto dto = new EventNoIdDto();
        dto.setName("Test Event");
        dto.setDescription("Event Description");
        dto.setMaxAttendances(100);
        dto.setOpen(true);
        dto.setLatitude(44.7866);
        dto.setLongitude(20.4489);
        dto.setDate(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000L));
        dto.setEventTypeId(1L);
        dto.setEventOrganizerId(1L);
        return dto;
    }

    @Test
    @Order(1)
    void shouldReturnBadRequestWhenCreatingEventWithoutAuth() throws Exception {
        EventNoIdDto dto = validEvent();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(2)
    void shouldGetAllEventsSuccessfully() throws Exception {
        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("sortBy", "date")
                        .param("sortDirection", "DESC"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenGettingNonexistentEventById() throws Exception {
        mockMvc.perform(get("/api/events/{id}", 99999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(4)
    void shouldReturnBadRequestWhenCreatingAgendaForInvalidEvent() throws Exception {
        String agendaJson = """
            [
                {
                    "name": "Activity 1",
                    "activityStart": 1690000000000,
                    "activityEnd": 1690003600000,
                    "description": "Description",
                    "location": "Hangar 1"
                }
            ]
        """;

        mockMvc.perform(post("/api/events/{id}/agenda", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(agendaJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
    void shouldReturnNotFoundWhenDeletingNonexistentEvent() throws Exception {
        mockMvc.perform(delete("/api/events/{id}", 99999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(6)
    void shouldReturnBadRequestWhenAddingInvalidActivity() throws Exception {
        String activityJson = """
            {
                "name": "Activity 2",
                "activityStart": 1690000000000,
                "activityEnd": 1690003600000,
                "description": "Description",
                "location": "Hangar 1"
            }
        """;

        mockMvc.perform(post("/api/events/{id}/agenda/activity", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(activityJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

