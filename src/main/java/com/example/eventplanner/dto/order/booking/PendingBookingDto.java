package com.example.eventplanner.dto.order.booking;

import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.model.utils.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingBookingDto {
    private long id;
    private ServiceDto service;
    private double price;
    private long date;
    private double duration;
    private Instant createdAt;
    private String bookerName;
    private String bookerEmail;
}