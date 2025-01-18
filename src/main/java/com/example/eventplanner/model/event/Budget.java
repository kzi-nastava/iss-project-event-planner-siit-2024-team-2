package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class Budget extends Entity {
    private double plannedSpending;
    private double currentSpent = 0;
    @ManyToOne(cascade={CascadeType.ALL})
    private ServiceProductCategory serviceProductCategory;
}
