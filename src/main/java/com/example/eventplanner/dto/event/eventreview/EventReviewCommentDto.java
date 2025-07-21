package com.example.eventplanner.dto.event.eventreview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventReviewCommentDto {
    private long id;
    private String comment;
}