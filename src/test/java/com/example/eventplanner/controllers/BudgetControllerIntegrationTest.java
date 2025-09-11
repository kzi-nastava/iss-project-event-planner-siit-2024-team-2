package com.example.eventplanner.controllers;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.repositories.event.BudgetRepository;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.serviceproduct.ProductRepository;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class BudgetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EventOrganizerRepository eventOrganizerRepository;

    @MockBean
    private AuthUtil authUtil;

    Budget budget;
    Product product;
    Long userId;

    @BeforeEach
    void setUp() {
        budget = new Budget();
        budget.setName("Test Budget");
        budget.setPlannedSpending(200.0);
        budget.setCurrentSpent(0.0);
        List<Purchase> purchases = new ArrayList<>();
        budget.setPurchases(purchases);
        budget.setBookings(List.of());

        // Create and save Event with matching organizer
        EventOrganizer organizer = new EventOrganizer();
        eventOrganizerRepository.save(organizer);
        Event event = new Event();
        event.setEventOrganizer(organizer);
        event.setBudgets(List.of(budget));
        event = eventRepository.save(event);
        budget = budgetRepository.save(budget);

        userId = organizer.getId();

        product = new Product();
        product.setImages(List.of());

    }

    @Test
    @Order(1)
    void addNewPurchase_shouldSucceed_whenValid() throws Exception {
        when(authUtil.getAuthenticatedUserId()).thenReturn(userId);

        product.setPrice(60.0);
        product.setDiscount(10.0);
        product = productRepository.save(product);

        PurchaseNoIdDto dto = new PurchaseNoIdDto();
        dto.setProductId(product.getId());
        dto.setPrice(50.0);

        mockMvc.perform(post("/api/budgets/{id}/purchases", budget.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        assertEquals("budget name check","Test Budget", budget.getName());
        Budget updatedBudget = budgetRepository.findById(budget.getId()).orElseThrow();
        assertEquals("budget purchases number", 1, updatedBudget.getPurchases().size());
        assertEquals("product total price", 50.0, product.getPrice() - product.getDiscount());
        assertEquals("current spent amount increased regular", 50.0, budget.getCurrentSpent());
    }

    @Test
    @Order(2)
    void addNewPurchase_shouldFail_whenTooHighAmount() throws Exception {
        when(authUtil.getAuthenticatedUserId()).thenReturn(userId);

        product.setPrice(300.0);
        product.setDiscount(10.0);
        product = productRepository.save(product);

        PurchaseNoIdDto dto = new PurchaseNoIdDto();
        dto.setProductId(product.getId());
        dto.setPrice(290.0);

        mockMvc.perform(post("/api/budgets/{id}/purchases", budget.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        assertEquals("budget name check","Test Budget", budget.getName());
        Budget updatedBudget = budgetRepository.findById(budget.getId()).orElseThrow();
        assertEquals("budget purchases number", 0, updatedBudget.getPurchases().size());
        assertEquals("product total price", 290.0, product.getPrice() - product.getDiscount());
        assertEquals("current spent amount stayed same", 0.0, budget.getCurrentSpent());
    }

    @Test
    @Order(3)
    void addNewPurchase_shouldReturnForbidden_whenUnauthorized() throws Exception {
        userId = userId + 1;
        when(authUtil.getAuthenticatedUserId()).thenReturn(userId);
        product = productRepository.save(product);
        PurchaseNoIdDto dto = new PurchaseNoIdDto();
        dto.setProductId(product.getId());
        dto.setPrice(100.0);

        mockMvc.perform(post("/api/budgets/{id}/purchases", budget.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    void addNewPurchase_shouldReturnNotFound_whenNonexistentBudget() throws Exception {
        when(authUtil.getAuthenticatedUserId()).thenReturn(userId);
        product = productRepository.save(product);
        PurchaseNoIdDto dto = new PurchaseNoIdDto();
        dto.setProductId(product.getId());
        dto.setPrice(100.0);

        mockMvc.perform(post("/api/budgets/999/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void addNewPurchase_shouldReturnNotFound_whenNonexistentProduct() throws Exception {
        when(authUtil.getAuthenticatedUserId()).thenReturn(userId);
        product = productRepository.save(product);
        PurchaseNoIdDto dto = new PurchaseNoIdDto();
        dto.setProductId(999);
        dto.setPrice(100.0);

        mockMvc.perform(post("/api/budgets/{id}/purchases",  budget.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}

