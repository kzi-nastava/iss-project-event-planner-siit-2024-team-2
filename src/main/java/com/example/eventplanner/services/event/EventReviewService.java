package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.eventreview.*;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventReview;
import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@NoArgsConstructor
public class EventReviewService {
    Map<Long, EventReview> eventReviews = new HashMap<Long, EventReview>();
    private long idCounter = 0;

    public List<EventReviewDto> getAll() {
        return eventReviews.values()
                .stream()
                .filter(Entity::isActive)
                .map(EventReviewMapper::toDto)
                .toList();
    }

    public EventReviewDto getById(long id) {
        if (!eventReviews.containsKey(id) || !eventReviews.get(id).isActive())
            return null;
        return EventReviewMapper.toDto(eventReviews.get(id));
    }

    public EventReviewDto create(EventReviewNoIdDto dto) {
        EventReview eventReview = EventReviewMapper.toEntity(dto);
        eventReview.setId(idCounter++);
        eventReview.setActive(true);
        // link user
        // link event
        eventReviews.put(eventReview.getId(), eventReview);
        return EventReviewMapper.toDto(eventReview);
    }

    public EventReviewDto update(EventReviewNoIdDto dto, long id) {
        if (this.getById(id) == null)
            return null;
        EventReview eventReview = EventReviewMapper.toEntity(dto);
        // link user
        // link event
        eventReviews.put(id, EventReviewMapper.toEntity(dto));
        return EventReviewMapper.toDto(eventReview);
    }

    public boolean delete(long id) {
        if (this.getById(id) == null)
            return false;
        eventReviews.get(id).setActive(false);
        return true;
    }

    public EventReviewStatusDto updateStatus(Long id, ReviewStatus status) {
        if (this.getById(id) == null)
            return null;
        eventReviews.get(id).setReviewStatus(status);
        return new EventReviewStatusDto(id, status);
    }

    public EventReviewCommentDto updateComment(Long id, String comment) {
        if (this.getById(id) == null)
            return null;
        eventReviews.get(id).setComment(comment);
        return new EventReviewCommentDto(id, comment);
    }
}
