package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private long activityStart;
    private long activityEnd;
    private String description;
    private String location;
}
