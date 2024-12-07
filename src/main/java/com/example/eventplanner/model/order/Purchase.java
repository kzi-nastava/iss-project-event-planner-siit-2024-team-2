package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("isActive = true")
@jakarta.persistence.Entity
public class Purchase extends Entity {
    @ManyToOne(cascade = {CascadeType.ALL})
    private Event event;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Product product;
    private double price;
}
