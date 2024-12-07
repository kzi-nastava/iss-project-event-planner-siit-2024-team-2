package com.example.eventplanner.dto.event.event;

import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventNoIdDto {
    private String name;
    private String description;
    private long typeId;
    private int maxAttendances;
    private boolean isOpen;
    private double longitude;
    private double latitude;
    private long date;
    private List<Long> activityIds;
    private List<Long> budgets;
}