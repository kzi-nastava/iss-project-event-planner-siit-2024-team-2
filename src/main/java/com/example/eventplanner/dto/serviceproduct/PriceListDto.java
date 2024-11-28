package com.example.eventplanner.dto.serviceproduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDto {
    private Long orderNumber;
    private String name;
    private double price;
    private double discount;
    private double total = price - discount;
}
