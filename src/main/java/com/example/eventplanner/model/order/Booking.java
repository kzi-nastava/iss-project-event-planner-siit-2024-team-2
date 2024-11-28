package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends Entity {
    private Event event;
    private Service service;
    private double price;
    private Date date;
    private double duration;
}
