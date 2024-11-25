package com.example.model.event;

import com.example.model.Entity;
import com.example.model.serviceproduct.ServiceProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventType extends Entity {
    private String name;
    private List<ServiceProduct> recommendedServiceProducts;
}
