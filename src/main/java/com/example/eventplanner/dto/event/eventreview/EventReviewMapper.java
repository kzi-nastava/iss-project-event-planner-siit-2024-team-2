package com.example.eventplanner.dto.event.eventreview;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventReview;
import com.example.eventplanner.model.user.BaseUser;

public class EventReviewMapper {
    public static EventReviewDto toDto(EventReview eventReview) {
        if (eventReview == null)
            return null;
        return new EventReviewDto(
                eventReview.getId(),
                eventReview.getGrade(),
                eventReview.getComment(),
                EventMapper.toDto(eventReview.getEvent()),
                eventReview.getReviewStatus()
        );
    }

    public static EventReview toEntity(EventReviewNoIdDto dto, BaseUser user, Event event) {
        if (dto == null)
            return null;
        return new EventReview(
                dto.getGrade(),
                dto.getComment(),
                user,
                event,
                dto.getReviewStatus());
    }
}