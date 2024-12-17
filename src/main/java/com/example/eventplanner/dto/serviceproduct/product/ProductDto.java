package com.example.eventplanner.dto.serviceproduct.product;

import com.example.eventplanner.dto.user.user.ServiceProductProviderDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.ServiceProductProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private ServiceProductProviderDto serviceProductProvider;
    private boolean available;
    private double price;
    private String name;
    private String description;
}
