package com.example.eventplanner.dto.event.activity;

import com.example.eventplanner.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
