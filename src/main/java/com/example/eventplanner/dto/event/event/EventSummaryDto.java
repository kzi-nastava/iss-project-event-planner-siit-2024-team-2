package com.example.eventplanner.dto.event.event;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDto {
    private long id;
    private String name;
    private String description;
    private EventTypeDto type;
    private int maxAttendances;
    private boolean isOpen;
    private double longitude;
    private double latitude;
    private long date;
    private String creatorName;
    private String creatorEmail;
}