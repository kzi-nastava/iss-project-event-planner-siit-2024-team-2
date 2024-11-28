package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase extends Entity {
    private Event event;
    private Product product;
    private double price;
}
