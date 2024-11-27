package com.example.eventplanner.dto.event.eventreview;

import com.example.eventplanner.model.event.EventReview;

public class EventReviewMapper {
    public static EventReviewDto toDto(EventReview eventReview) {
        if (eventReview == null)
            return null;
        return new EventReviewDto(
                eventReview.getId(),
                eventReview.getGrade(),
                eventReview.getComment(),
                eventReview.getUser().getId(),
                eventReview.getEvent().getId(),
                eventReview.getReviewStatus()
        );
    }

    public static EventReviewNoIdDto toDtoNoId(EventReview eventReview) {
        if (eventReview == null)
            return null;
        return new EventReviewNoIdDto(
                eventReview.getGrade(),
                eventReview.getComment(),
                eventReview.getUser().getId(),
                eventReview.getEvent().getId(),
                eventReview.getReviewStatus()
        );
    }

    public static EventReview toEntity(EventReviewDto dto) {
        if (dto == null)
            return null;
        EventReview eventReview = new EventReview(
                dto.getGrade(),
                dto.getComment(),
                null,
                null,
                dto.getReviewStatus());
        eventReview.setId(dto.getId());
        eventReview.setActive(true);
        return eventReview;
    }

    public static EventReview toEntity(EventReviewNoIdDto dto) {
        if (dto == null)
            return null;
        EventReview eventReview = new EventReview(
                dto.getGrade(),
                dto.getComment(),
                null,
                null,
                dto.getReviewStatus());
        eventReview.setActive(true);
        return eventReview;
    }
}