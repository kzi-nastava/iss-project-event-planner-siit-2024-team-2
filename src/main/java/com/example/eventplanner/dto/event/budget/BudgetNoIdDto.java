package com.example.eventplanner.dto.event.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetNoIdDto {
    private double plannedSpending;
    private double currentSpent;
    private long serviceProductCategoryId;
}
