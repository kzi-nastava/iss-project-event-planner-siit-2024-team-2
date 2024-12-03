package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class Budget extends Entity {
    private double plannedSpending;
    private double maxAmount;
    @ManyToOne(cascade={CascadeType.ALL})
    private ServiceProductCategory serviceProductCategory;
}
