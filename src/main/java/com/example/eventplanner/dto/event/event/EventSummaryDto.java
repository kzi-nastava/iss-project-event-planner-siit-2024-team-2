package com.example.eventplanner.dto.event.event;

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
public class EventSummaryDto {
    private long id;
    private String name;
    private String description;
    private long typeId;
    private int maxAttendances;
    private boolean isOpen;
    private double longitude;
    private double latitude;
    private Date date;
}