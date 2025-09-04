package com.example.eventplanner.dto.event.budget;

import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDto {
    private long id;
    private String name;
    private double plannedSpending;
    private double currentSpent;
    private ServiceProductCategoryDto serviceProductCategory;
    private List<BookingDto> bookings;
    private List <PurchaseDto> purchases;
}
