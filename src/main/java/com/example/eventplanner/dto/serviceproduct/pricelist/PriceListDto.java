package com.example.eventplanner.dto.serviceproduct.pricelist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceListDto {
    private Long id;
    private String name;
    private Double price;
    private Double discount;
    private Double total;

    public PriceListDto(Long id, String name, Double price, Double discount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.total = price - discount;
    }
}
