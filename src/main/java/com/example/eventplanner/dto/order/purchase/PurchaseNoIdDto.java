package com.example.eventplanner.dto.order.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseNoIdDto {
    private long eventId;
    private long productId;
    private double price;
}
