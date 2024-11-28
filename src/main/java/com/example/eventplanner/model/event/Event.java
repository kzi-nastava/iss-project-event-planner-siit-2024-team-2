package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event extends Entity {
    private String name;
    private String description;
    private EventType type;
    private int maxAttendances;
    private boolean open;
    private double longitude;
    private double latitude;
    private Date date;
    private List<Activity> activities;
    private List<Budget> budgets;
}
