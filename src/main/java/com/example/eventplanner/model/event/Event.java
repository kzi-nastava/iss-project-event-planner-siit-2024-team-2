package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class Event extends Entity {
    private String name;
    private String description;
    @ManyToOne
    private EventType type;
    private int maxAttendances;
    private boolean open;
    private double longitude;
    private double latitude;
    private Date date;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Activity> activities;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Budget> budgets;
}
