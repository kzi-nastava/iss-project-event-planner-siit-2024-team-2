package com.example.eventplanner.dto.event.budget;

import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;

public class BudgetMapper {

    public static BudgetDto toDto(Budget budget) {
        if (budget == null) {
            return null;
        }
        return new BudgetDto(budget.getId(), budget.getPlannedSpending(), budget.getMaxAmount(),
                ServiceProductCategoryMapper.toDto(budget.getServiceProductCategory()));
    }

    public static Budget toEntity(BudgetDto budgetDto, ServiceProductCategory category) {
        if (budgetDto == null) {
            return null;
        }
        Budget budget = new Budget();
        budget.setId(budgetDto.getId());
        budget.setPlannedSpending(budgetDto.getPlannedSpending());
        budget.setMaxAmount(budgetDto.getMaxAmount());
        budget.setServiceProductCategory(category);
        return budget;
    }

    public static Budget toEntity(BudgetNoIdDto budgetDto, ServiceProductCategory category) {
        if (budgetDto == null) {
            return null;
        }
        Budget budget = new Budget();
        budget.setPlannedSpending(budgetDto.getPlannedSpending());
        budget.setMaxAmount(budgetDto.getMaxAmount());
        budget.setServiceProductCategory(category);
        return budget;
    }
}
