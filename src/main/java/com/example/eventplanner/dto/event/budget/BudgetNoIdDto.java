package com.example.eventplanner.dto.event.budget;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetNoIdDto {
    @NotBlank(message = "Name is required")
    @Length(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    @Min(value = 0, message = "Planned spending must be greater or equal to 0")
    private double plannedSpending;
    @Min(value = 1, message = "Service product category is required")
    private long serviceProductCategoryId;
}
