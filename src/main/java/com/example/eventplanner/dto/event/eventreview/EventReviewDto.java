package com.example.eventplanner.dto.event.eventreview;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.user.BaseUser;
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
    private EventDto event;
    private ReviewStatus reviewStatus;
}