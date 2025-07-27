package com.example.eventplanner.dto.event.event;

import com.example.eventplanner.dto.event.activity.ActivityDto;
import com.example.eventplanner.dto.event.budget.BudgetDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.user.user.BaseUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private long id;
    private String name;
    private String description;
    private EventTypeDto type;
    private BaseUserDto eventOrganizerDto;
    private int maxAttendances;
    private boolean isOpen;
    private double longitude;
    private double latitude;
    private long date;
    private List<ActivityDto> activity;
    private List<BudgetDto> budgets;
}