package com.example.eventplanner.dto.order.booking;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingNoIdDto {
    private long eventId;
    private long serviceId;
    private double price;
    private long date;
    private double duration;
}
