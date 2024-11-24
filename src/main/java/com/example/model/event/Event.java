package com.example.model.event;

import com.example.model.Entity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event extends Entity {
    private boolean isOpen;
    private double plannedSpending;
    private List<Activity> activities;
    private List<Budget> budgets;
}
