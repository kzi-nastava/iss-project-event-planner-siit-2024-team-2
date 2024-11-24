package com.example.model.event;

import com.example.model.Entity;
import com.example.model.serviceproduct.ServiceProductCategory;
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
