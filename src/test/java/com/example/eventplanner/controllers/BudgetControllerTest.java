package com.example.eventplanner.controllers;

import com.example.eventplanner.dto.event.budget.BudgetDto;
import com.example.eventplanner.dto.event.budget.BudgetNoIdDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.services.event.BudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BudgetService budgetService;

    private Long budgetId;
    private BudgetDto budget;
    private BudgetDto budget2;

    @BeforeEach
    void setUp() {
        ServiceProductCategoryDto category = new ServiceProductCategoryDto();
        category.setId(1L);
        category.setName("Category 1");

        budget = new BudgetDto();
        budget.setId(1L);
        budget.setName("Budget 1");
        budget.setCurrentSpent(0);
        budget.setPlannedSpending(100);
        budget.setServiceProductCategory(category);
        budgetId = budget.getId();

        budget2 = new BudgetDto();
        budget2.setId(2L);
        budget2.setName("Budget 2");
        budget2.setCurrentSpent(0);
        budget2.setPlannedSpending(200);
        budget2.setServiceProductCategory(category);
    }

    @Test
    void getAllBudgets_shouldSucceed() throws Exception {
        when(budgetService.getAll()).thenReturn(List.of(budget, budget2));

        mockMvc.perform(get("/api/budgets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].plannedSpending").value(100.0))
                .andExpect(jsonPath("$[0].serviceProductCategory.name").value("Category 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Budget 2"));
    }

    @Test
    void createBudget_shouldSucceed() throws Exception {
        BudgetNoIdDto dto = new BudgetNoIdDto();
        dto.setServiceProductCategoryId(1L);
        dto.setName("Budget 1");

        when(budgetService.create(any(BudgetNoIdDto.class))).thenReturn(budget);

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Budget 1"))
                .andExpect(jsonPath("$.plannedSpending").value(100.0))
                .andExpect(status().isCreated());
    }

    @Test
    void createBooking_ShouldReturnBadRequest() throws Exception {
        when(budgetService.create(any())).thenReturn(null);
        BudgetNoIdDto dto = new BudgetNoIdDto();    // required fields missed

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void getBudgetById_shouldSucceed() throws Exception {
        when(budgetService.getById(budgetId)).thenReturn(budget);

        mockMvc.perform(get("/api/budgets/{id}", budgetId))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.currentSpent").value(0))
                .andExpect(jsonPath("$.serviceProductCategory.name").value("Category 1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBudgetByNonexistentId_shouldReturnNotFound() throws Exception {
        when(budgetService.getById(budgetId)).thenReturn(null);

        mockMvc.perform(get("/api/budgets/{id}", budgetId))
                .andExpect(status().isNotFound());
    }

    @Test
    void setNewAmount_shouldSucceed() throws Exception {
        mockMvc.perform(put("/api/budgets/{id}", budgetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("500.0"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewBooking_shouldFail_whenInvalidBooking() throws Exception {
        BookingNoIdDto bookingDto = new BookingNoIdDto();

        mockMvc.perform(post("/api/budgets/{id}/bookings", budgetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void addNewBooking_shouldFail_whenExceedsBudget() throws Exception {
        BookingNoIdDto bookingDto = new BookingNoIdDto();
        bookingDto.setServiceId(1L);
        bookingDto.setPrice(200); // exceeds budget
        bookingDto.setDuration(5);
        bookingDto.setDate(Instant.now());

        doThrow(new IllegalArgumentException("Not enough money"))
                .when(budgetService).addBookingToBudget(eq(budgetId), any(BookingNoIdDto.class));

        mockMvc.perform(post("/api/budgets/{id}/bookings", budgetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addNewBooking_shouldSucceed_whenWithinBudget() throws Exception {
        BookingNoIdDto bookingDto = new BookingNoIdDto();
        bookingDto.setServiceId(1L);
        bookingDto.setPrice(50);
        bookingDto.setDuration(5);
        bookingDto.setDate(Instant.now());

        doNothing().when(budgetService).addBookingToBudget(budgetId, bookingDto);

        mockMvc.perform(post("/api/budgets/{id}/bookings", budgetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());
    }

    @Test
    void addNewPurchase_shouldSucceed_whenWithinBudget() throws Exception {
        PurchaseNoIdDto purchaseDto = new PurchaseNoIdDto();
        purchaseDto.setProductId(1L);
        purchaseDto.setPrice(50);

        doNothing().when(budgetService).addPurchaseToBudget(budgetId, purchaseDto);

        mockMvc.perform(post("/api/budgets/{id}/purchases", budgetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDto)))
                .andExpect(status().isOk());
    }

    @Test
    void addNewPurchase_shouldFail_whenInvalidPurchase() throws Exception {
        PurchaseNoIdDto purchaseDto = new PurchaseNoIdDto();

        doNothing().when(budgetService).addPurchaseToBudget(budgetId, purchaseDto);

        mockMvc.perform(post("/api/budgets/{id}/purchases", budgetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ValidationError"));
    }

    @Test
    void deleteBudgetById_shouldReturnNoContent() throws Exception {
        when(budgetService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/budgets/{id}", budgetId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBudgetByNonexistingId_shouldReturnNotFound() throws Exception {
        when(budgetService.delete(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/budgets/{id}",  budgetId))
                .andExpect(status().isNotFound());
    }
}
