package com.example.eventplanner.controllers;

import com.example.eventplanner.dto.util.DateRangeDto;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceService serviceService;

    @Test
    void getAvailableDates_ShouldReturnOk() throws Exception {
        when(serviceService.getAvailableDates(any(), any())).thenReturn(List.of(new DateRangeDto(1L, 2L)));

        mockMvc.perform(get("/api/services/1/availability")
                        .param("eventId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].start").value(1L))
                .andExpect(jsonPath("$[0].end").value(2L));
    }

}