package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.utils.BookingStatus;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
@Table(name = "booking", indexes = {
        @Index(name = "idx_booking_reminder_check", columnList = "date, status, sentReminder")
})
public class Booking extends Entity {
    @ManyToOne
    private Service service;
    private double price;
    private Instant date;
    private double duration;
    private BookingStatus status;
    private Instant createdAt;
    @ColumnDefault("false")
    private boolean sentReminder;
}
