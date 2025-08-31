package com.example.eventplanner.dto.event.budget;

import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.order.purchase.PurchaseMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;

import java.util.List;

public class BudgetMapper {
    private BudgetMapper() {}

    public static BudgetDto toDto(Budget budget) {
        if (budget == null)
            return null;

        return new BudgetDto(budget.getId(), budget.getName(), budget.getPlannedSpending(), budget.getCurrentSpent(),
                ServiceProductCategoryMapper.toDto(budget.getServiceProductCategory()),
                budget.getBookings().stream().map(BookingMapper::toDto).toList(),
                budget.getPurchases().stream().map(PurchaseMapper::toDto).toList());
    }

    public static Budget toEntity(BudgetNoIdDto budgetDto, ServiceProductCategory category,
                                  Double currentSpent, List<Booking> bookings, List<Purchase> purchases) {
        if (budgetDto == null)
            return null;

        return new Budget(
                budgetDto.getName(),
                budgetDto.getPlannedSpending(),
                currentSpent,
                category,
                bookings,
                purchases);
    }
}
