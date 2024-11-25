package com.example.model.serviceproduct;

import com.example.model.Entity;
import com.example.model.user.ServiceProductProvider;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProduct extends Entity {
    private boolean available;
    private double price;
    private String name;
    private String description;
    private ServiceProductProvider serviceProductProvider;
}
