package com.example.eventplanner.dto.event.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventNoIdDto {
    private String name;
    private String description;
    private long eventTypeId;
    private long eventOrganizerId;
    private int maxAttendances;
    private boolean open;
    private double longitude;
    private double latitude;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<Long> activityIds;
    private List<Long> budgetIds;
    private List<String> invitationEmails;
}