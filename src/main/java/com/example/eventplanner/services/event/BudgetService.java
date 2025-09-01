package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.budget.BudgetDto;
import com.example.eventplanner.dto.event.budget.BudgetMapper;
import com.example.eventplanner.dto.event.budget.BudgetNoIdDto;
import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.order.purchase.PurchaseMapper;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.repositories.event.BudgetRepository;
import com.example.eventplanner.repositories.order.BookingRepository;
import com.example.eventplanner.repositories.order.PurchaseRepository;
import com.example.eventplanner.repositories.serviceproduct.ProductRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductCategoryRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final ServiceProductCategoryRepository serviceProductCategoryRepository;
    private final BookingRepository bookingRepository;
    private final PurchaseRepository purchaseRepository;
    private final ServiceRepository serviceRepository;
    private final ProductRepository productRepository;

    public List<BudgetDto> getAll() {
        return budgetRepository.findAll()
                .stream().map(BudgetMapper::toDto).toList();
    }

    public BudgetDto getById(Long id) {
        return BudgetMapper.toDto(budgetRepository.findById(id)
                .orElse(null));
    }

    public BudgetDto create(BudgetNoIdDto dto) {
        ServiceProductCategory category = serviceProductCategoryRepository.getReferenceById(dto.getServiceProductCategoryId());
        List<Booking> bookings = Collections.emptyList();
        List<Purchase> purchases = Collections.emptyList(); // both of these lists are initially empty
        Budget budget = BudgetMapper.toEntity(dto, category, 0.0, bookings, purchases);
        Budget savedBudget = budgetRepository.save(budget);
        return BudgetMapper.toDto(savedBudget);
    }

    @Transactional
    public void setNewAmount(Long id, Double newAmount) {
        if (newAmount == null || newAmount < 0) {
            throw new IllegalArgumentException("Planned spending must be a non-negative number");
        }
        budgetRepository.setNewAmount(id, newAmount);
    }

    @Transactional
    public void addBookingToBudget(Long budgetId, BookingNoIdDto bookingDto) {
        // create new booking
        com.example.eventplanner.model.serviceproduct.Service service = serviceRepository.getReferenceById(bookingDto.getServiceId());
        Booking booking = BookingMapper.toEntity(bookingDto, service);
        bookingRepository.save(booking);

        // add booking to the budget
        Budget budget = budgetRepository.findById(budgetId).orElse(null);
        budget.getBookings().add(booking);
        budget.setCurrentSpent(budget.getCurrentSpent() + booking.getPrice());
        budgetRepository.save(budget);
    }

    @Transactional
    public void addPurchaseToBudget(Long budgetId, PurchaseNoIdDto purchaseDto) {
        // create new purchase
        Product product = productRepository.getReferenceById(purchaseDto.getProductId());
        Purchase purchase = PurchaseMapper.toEntity(purchaseDto, product);
        purchaseRepository.save(purchase);

        // add purchase to the budget
        Budget budget = budgetRepository.findById(budgetId).orElse(null);
        budget.getPurchases().add(purchase);
        budget.setCurrentSpent(budget.getCurrentSpent() + purchase.getPrice());
        budgetRepository.save(budget);
    }

    public boolean delete(long id) {
        if (!budgetRepository.existsById(id)) {
            return false;
        }
        budgetRepository.deleteById(id);
        return true;
    }
}
