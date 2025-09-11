package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class Budget extends Entity {
    private String name;
    private double plannedSpending;
    private double currentSpent = 0;
    @ManyToOne
    private ServiceProductCategory serviceProductCategory;
    @OneToMany
    @JoinTable(name = "budget_booking",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "bookings_id"))
    private List<Booking> bookings;
    @OneToMany
    private List<Purchase> purchases;
}
