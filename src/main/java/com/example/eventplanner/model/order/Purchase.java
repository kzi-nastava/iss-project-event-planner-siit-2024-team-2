package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@SQLRestriction("isActive = true")
public class Purchase extends Entity {
    @ManyToOne(cascade = {CascadeType.ALL})
    private Event event;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Product product;
    private double price;
}
