package com.example.eventplanner.dto.event.budget;

import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDto {
    private long id;
    private double plannedSpending;
    private double maxAmount;
    private ServiceProductCategoryDto serviceProductCategory;
}
