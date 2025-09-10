package com.example.eventplanner.controllers;

import com.example.eventplanner.controllers.order.BookingController;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.order.booking.PendingBookingDto;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.services.order.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDto bookingDto;
    private BookingNoIdDto bookingNoIdDto;
    private Service service;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingNoIdDto = new BookingNoIdDto();
        bookingNoIdDto.setServiceId(1L);
        bookingNoIdDto.setPrice(100.0);
        bookingNoIdDto.setDuration(1);
        bookingNoIdDto.setDate(Instant.now());
    }

    @Test
    void getAllBookings_ShouldReturnOk() throws Exception {
        when(bookingService.getAll()).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()));
    }

    @Test
    void getBookingById_ShouldReturnOk() throws Exception {
        when(bookingService.getById(1L)).thenReturn(bookingDto);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void getBookingById_ShouldReturnNotFound() throws Exception {
        when(bookingService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/bookings/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBooking_ShouldReturnCreated() throws Exception {
        when(bookingService.create(any())).thenReturn(bookingDto);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingNoIdDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void createBooking_ShouldReturnBadRequest_WhenDurationInvalid() throws Exception {
        when(bookingService.create(any())).thenReturn(null);
        bookingNoIdDto.setDuration(-1);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingNoIdDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void createBooking_ShouldReturnBadRequest_WhenServiceIdInvalid() throws Exception {
        when(bookingService.create(any())).thenReturn(null);
        bookingNoIdDto.setServiceId(0L);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingNoIdDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void createBooking_ShouldReturnBadRequest_WhenPriceInvalid() throws Exception {
        when(bookingService.create(any())).thenReturn(null);
        bookingNoIdDto.setPrice(-1);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingNoIdDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void createBooking_ShouldReturnBadRequest_WhenDateInvalid() throws Exception {
        when(bookingService.create(any())).thenReturn(null);
        bookingNoIdDto.setDate(null);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingNoIdDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void updateBooking_ShouldReturnOk() throws Exception {
        when(bookingService.update(any(), eq(1L))).thenReturn(bookingDto);

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingNoIdDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void updateBooking_ShouldReturnNotFound() throws Exception {
        when(bookingService.update(any(), eq(99L))).thenReturn(null);

        mockMvc.perform(put("/api/bookings/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingNoIdDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBooking_ShouldReturnNoContent() throws Exception {
        when(bookingService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBooking_ShouldReturnNotFound() throws Exception {
        when(bookingService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/bookings/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void acceptBooking_ShouldReturnOk() throws Exception {
        when(bookingService.accept(1L)).thenReturn(bookingDto);

        mockMvc.perform(post("/api/bookings/1/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void getMyBookings_ShouldReturnOk() throws Exception {
        PendingBookingDto pendingBooking = new PendingBookingDto();
        PageImpl<PendingBookingDto> page = new PageImpl<>(List.of(pendingBooking),
                PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"createdAt")), 1);

        when(bookingService.getMyBookings(any())).thenReturn(page);

        mockMvc.perform(get("/api/bookings/mine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").exists());
    }

    @Test
    void getMyBookings_ShouldReturnBadRequest_WhenPageInvalid() throws Exception {
        mockMvc.perform(get("/api/bookings/mine").param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMyBookings_ShouldReturnBadRequest_WhenSizeInvalid() throws Exception {
        mockMvc.perform(get("/api/bookings/mine").param("size", "-1"))
                .andExpect(status().isBadRequest());
    }
}