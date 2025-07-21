package com.example.eventplanner.dto.event.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private String name;
    private long activityStart;
    private long activityEnd;
    private String description;
    private String location;
}
