package com.example.eventplanner.dto.serviceproduct.pricelist;

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

    public PriceListDto(Long orderNumber, CreatePriceListDto createPriceListDto) {
        this.orderNumber = orderNumber;
        this.name = createPriceListDto.getName();
        this.price = createPriceListDto.getPrice();
        this.discount = createPriceListDto.getDiscount();
        this.total = price - discount;
    }
}
