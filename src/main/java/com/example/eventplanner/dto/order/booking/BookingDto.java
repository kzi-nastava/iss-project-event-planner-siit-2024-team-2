package com.example.eventplanner.dto.order.booking;

import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private long id;
    private ServiceDto service;
    private double price;
    private long date;
    private double duration;
}
