package com.example.eventplanner.model.serviceproduct;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service extends ServiceProduct {
    private String specifies;
    private float duration;
    private float minEngagementDuration;
    private float maxEngagementDuration;
    private int reservationDaysDeadline;
    private int cancellationDaysDeadline;
    private boolean hasAutomaticReservation;
}
