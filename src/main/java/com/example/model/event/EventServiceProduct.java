package com.example.model.event;

import com.example.model.Entity;
import com.example.model.serviceproduct.ServiceProduct;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventServiceProduct extends Entity {
    private double price;
    private Event event;
    private ServiceProduct serviceProduct;
}
