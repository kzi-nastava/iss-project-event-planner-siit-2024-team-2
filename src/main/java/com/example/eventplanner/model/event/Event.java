package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
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
    @ManyToOne(cascade = {CascadeType.ALL})
    private EventOrganizer eventOrganizer;
}
