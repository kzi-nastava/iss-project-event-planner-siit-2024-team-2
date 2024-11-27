package com.example.eventplanner.dto.event.eventreview;

import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventReviewNoIdDto {
    private int grade;
    private String comment;
    private long userId;
    private long eventId;
    private ReviewStatus reviewStatus;
}