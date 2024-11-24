package com.example.model.event;

import com.example.model.Entity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends Entity {
    private String name;
    private Date activityStart;
    private Date activityEnd;
    private String description;
    private String location;
}
