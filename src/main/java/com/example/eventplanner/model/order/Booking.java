package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.utils.BookingStatus;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class Booking extends Entity {
    @ManyToOne
    private Service service;
    private double price;
    private Date date;
    private double duration;
    private BookingStatus status;
    private Instant createdAt;
}
