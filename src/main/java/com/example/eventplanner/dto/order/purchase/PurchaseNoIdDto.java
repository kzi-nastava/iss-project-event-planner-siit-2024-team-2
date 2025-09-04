package com.example.eventplanner.dto.order.purchase;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseNoIdDto {
    @Min(value = 1, message = "Product is required")
    private long productId;
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;
}
