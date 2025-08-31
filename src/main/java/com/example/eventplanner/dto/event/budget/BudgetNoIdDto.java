package com.example.eventplanner.dto.event.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetNoIdDto {
    private String name;
    private double plannedSpending;
    private long serviceProductCategoryId;
}
