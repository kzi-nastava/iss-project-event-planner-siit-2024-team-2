package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class Activity extends Entity {
    private String name;
    private Date activityStart;
    private Date activityEnd;
    private String description;
    private String location;
}
