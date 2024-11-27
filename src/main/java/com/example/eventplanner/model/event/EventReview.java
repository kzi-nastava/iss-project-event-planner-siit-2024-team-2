package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventReview extends Entity {
    private int grade;
    private String comment;
    private User user;
    private Event event;
    private ReviewStatus reviewStatus;
}
