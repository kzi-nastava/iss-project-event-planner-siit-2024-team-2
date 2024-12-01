package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ReviewStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class EventReview extends Entity {
    private int grade;
    private String comment;
    @ManyToOne(cascade = {CascadeType.ALL})
    private BaseUser user;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Event event;
    private ReviewStatus reviewStatus;
}
