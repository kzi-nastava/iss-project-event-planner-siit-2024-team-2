package com.example.eventplanner.dto.serviceproduct.pricelist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePriceListDto {
    private String name;
    private double price;
    private double discount;
    private double total = price - discount;
}
