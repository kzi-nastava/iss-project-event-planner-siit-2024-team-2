package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Budget extends Entity {
    private double plannedSpending;
    private double maxAmount;
    private ServiceProductCategory serviceProductCategory;
}
