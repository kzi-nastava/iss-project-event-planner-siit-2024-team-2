package com.example.eventplanner.services;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.order.OrderEligibilityDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.serviceproduct.ServiceProductService;
import com.example.eventplanner.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceProductServiceTest {

    @Mock
    EventRepository eventRepository;
    @Mock
    ServiceProductRepository serviceProductRepository;
    @Mock
    AuthUtil authUtil;
    @Mock
    UserService userService;

    @InjectMocks
    ServiceProductService serviceProductService;

    EventOrganizer eventOrganizer;
    ServiceProduct serviceProduct;

    final static long serviceProductId = 10L;
    final static long providerId = 11L;
    final static long organizerId = 12L;

    @BeforeEach
    void setUp() {
        ServiceProductProvider serviceProductProvider = new ServiceProductProvider();
        serviceProductProvider.setId(providerId);
        serviceProductProvider.setEmail("provider@example.com");

        serviceProduct = new Product();
        serviceProduct.setId(serviceProductId);
        serviceProduct.setName("Test Service");
        serviceProduct.setPrice(100.0);
        serviceProduct.setDiscount(10.0);
        serviceProduct.setAvailable(true);
        serviceProduct.setVisible(true);
        serviceProduct.setServiceProductProvider(serviceProductProvider);

        eventOrganizer = new EventOrganizer();
        eventOrganizer.setId(organizerId);
        eventOrganizer.setEmail("organizer@example.com");
        eventOrganizer.setUserRole(UserRole.EVENT_ORGANIZER);
    }

    @Test
    void getOrderEligibility_ShouldReturnOk_WhenEligible() {
        when(serviceProductRepository.findById(serviceProductId)).thenReturn(Optional.of(serviceProduct));
        when(authUtil.getAuthenticatedUser()).thenReturn(eventOrganizer);
        when(userService.hasBlocked(anyLong(), anyLong())).thenReturn(false);

        OrderEligibilityDto result = serviceProductService.canOrderServiceProduct(serviceProductId);
        assertTrue(result.isCanOrder());
    }

    @Test
    void getOrderEligibility_ShouldReturnFalse_WhenNotAuthorized() {
        when(authUtil.getAuthenticatedUser()).thenReturn(null);

        OrderEligibilityDto result = serviceProductService.canOrderServiceProduct(serviceProductId);
        assertFalse(result.isCanOrder());
    }

    @Test
    void getOrderEligibility_ShouldReturnFalse_WhenNotAvailable() {
        serviceProduct.setAvailable(false);
        when(serviceProductRepository.findById(serviceProductId)).thenReturn(Optional.of(serviceProduct));
        when(authUtil.getAuthenticatedUser()).thenReturn(eventOrganizer);

        OrderEligibilityDto result = serviceProductService.canOrderServiceProduct(serviceProductId);
        assertFalse(result.isCanOrder());
    }

    @Test
    void getOrderEligibility_ShouldReturnFalse_WhenBlocked() {
        when(serviceProductRepository.findById(serviceProductId)).thenReturn(Optional.of(serviceProduct));
        when(authUtil.getAuthenticatedUser()).thenReturn(eventOrganizer);
        when(userService.hasBlocked(anyLong(), anyLong())).thenReturn(true);

        OrderEligibilityDto result = serviceProductService.canOrderServiceProduct(serviceProductId);
        assertFalse(result.isCanOrder());
    }
}
