package com.example.eventplanner.dto.order.purchase;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseNoIdDto {
    private EventDto event;
    private ProductDto product;
    private double price;
}
