package com.example.eventplanner.model.serviceproduct;

import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@Entity
public class Service extends ServiceProduct {
    private String specifies;
    private float duration;
    private float minEngagementDuration;
    private float maxEngagementDuration;
    private int reservationDaysDeadline;
    private int cancellationDaysDeadline;
    private boolean hasAutomaticReservation;
}
