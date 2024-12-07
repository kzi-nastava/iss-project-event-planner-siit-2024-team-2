package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class Activity extends Entity {
    private String name;
    private Date activityStart;
    private Date activityEnd;
    private String description;
    private String location;
}
