package com.example.eventplanner.dto.order.booking;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingNoIdDto {
    @Min(value = 1, message = "Service is required")
    private long serviceId;
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;
    @Range(min = 0, max = 24, message = "Duration must be between 0 and 24 hours")
    @DecimalMin(value = "0.0", inclusive = false, message = "Duration must be greater than 0")
    private double duration;
    @NotNull(message = "Date is required")
    private Instant date;
}
