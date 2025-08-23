package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.budget.BudgetDto;
import com.example.eventplanner.dto.event.budget.BudgetMapper;
import com.example.eventplanner.dto.event.budget.BudgetNoIdDto;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.repositories.event.BudgetRepository;
import com.example.eventplanner.repositories.order.BookingRepository;
import com.example.eventplanner.repositories.order.PurchaseRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final ServiceProductCategoryRepository serviceProductCategoryRepository;
    private final BookingRepository bookingRepository;
    private final PurchaseRepository purchaseRepository;

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
        dto.setCurrentSpent(0); // each budget has 0 current spent by creation
        List<Booking> bookings = Collections.emptyList();
        List<Purchase> purchases = Collections.emptyList(); // both of these lists are initially empty
        Budget budget = BudgetMapper.toEntity(dto, category, bookings, purchases);
        Budget savedBudget = budgetRepository.save(budget);
        return BudgetMapper.toDto(savedBudget);
    }

    public BudgetDto update(Long id, BudgetNoIdDto dto) {
        return budgetRepository.findById(id)
                .map(budget -> {
                    budget.setId(id);
                    budget.setActive(true);
                    budget.setName(dto.getName());
                    budget.setCurrentSpent(dto.getCurrentSpent());
                    budget.setPlannedSpending(dto.getPlannedSpending());
                    serviceProductCategoryRepository.findById(dto.getServiceProductCategoryId()).ifPresent(budget::setServiceProductCategory);

                    budget.setBookings(dto.getBookings().stream()
                            .map(bookid -> bookingRepository.findById(bookid)
                                    .orElseThrow(() -> new RuntimeException("Booking not found: " + bookid)))
                            .collect(Collectors.toList()));

                    budget.setPurchases(dto.getPurchases().stream()
                            .map(purid -> purchaseRepository.findById(purid)
                                    .orElseThrow(() -> new RuntimeException("Purchase not found: " + purid)))
                            .collect(Collectors.toList()));
                    return BudgetMapper.toDto(budgetRepository.save(budget));
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        if (!budgetRepository.existsById(id)) {
            return false;
        }
        budgetRepository.deleteById(id);
        return true;
    }
}
