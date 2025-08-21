package com.example.eventplanner.dto.serviceproduct.product;

import com.example.eventplanner.dto.user.user.ServiceProductProviderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private long id;
    private ServiceProductProviderDto serviceProductProvider;
    private boolean available;
    private double price;
    private double discount;
    private String name;
    private String description;
    private List<String> images;
    private List<String> imageEncodedNames;
}
