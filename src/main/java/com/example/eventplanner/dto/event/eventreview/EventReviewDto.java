package com.example.eventplanner.dto.event.eventreview;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventReview;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventReviewDto {
    private long id;
    private int grade;
    private String comment;
    private long userId;
    private long eventId;
    private ReviewStatus reviewStatus;
}