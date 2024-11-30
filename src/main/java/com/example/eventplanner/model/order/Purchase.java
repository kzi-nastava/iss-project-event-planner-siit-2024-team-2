package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class Purchase extends Entity {
    @ManyToOne(cascade = {CascadeType.ALL})
    private Event event;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Product product;
    private double price;
}
