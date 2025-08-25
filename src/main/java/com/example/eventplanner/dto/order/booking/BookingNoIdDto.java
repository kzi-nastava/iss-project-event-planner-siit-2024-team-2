package com.example.eventplanner.dto.order.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingNoIdDto {
    private long serviceId;
    private double price;
    private long date;
    private double duration;
}
