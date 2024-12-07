package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.eventreview.*;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventReview;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.event.EventReviewRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class EventReviewService {
    private final EventReviewRepository eventReviewRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<EventReviewDto> getAll() {
        return eventReviewRepository.findAll()
                .stream()
                .map(EventReviewMapper::toDto)
                .toList();
    }

    public EventReviewDto getById(long id) {
        return eventReviewRepository.findById(id)
                .map(EventReviewMapper::toDto)
                .orElse(null);
    }

    public EventReviewDto create(EventReviewNoIdDto dto) {
        EventReview eventReview = EventReviewMapper.toEntity(dto);
        eventReview.setActive(true);

        // link user
        BaseUser user = userRepository.findById(dto.getUserId()).orElse(null);
        eventReview.setUser(user);
        // link event
        Event testEvent = eventRepository.findById(dto.getEventId()).orElse(null);
        eventReview.setEvent(testEvent);

        eventReviewRepository.save(eventReview);
        return EventReviewMapper.toDto(eventReview);
    }

    public EventReviewDto update(EventReviewNoIdDto dto, long id) {
        return eventReviewRepository.findById(id)
                .map(e -> {
                    EventReview eventReview = new EventReview();
                    eventReview.setId(id);
                    eventReview.setActive(true);
                    eventReview.setReviewStatus(dto.getReviewStatus());
                    eventReview.setComment(dto.getComment());
                    eventReview.setGrade(dto.getGrade());
                    BaseUser baseUser = new BaseUser();
                    baseUser.setId(dto.getUserId());
                    eventReview.setUser(baseUser);
                    userRepository.findById(dto.getUserId()).ifPresent(eventReview::setUser);
                    eventRepository.findById(dto.getEventId()).ifPresent(eventReview::setEvent);
                    EventReview updatedEventReview = eventReviewRepository.save(eventReview);
                    return EventReviewMapper.toDto(updatedEventReview);
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        return eventReviewRepository.findById(id)
                .map(eventReview -> {
                    eventReview.setActive(false);
                    eventReviewRepository.save(eventReview);
                    return true;
                })
                .orElse(false);
    }

    public EventReviewStatusDto updateStatus(Long id, ReviewStatus status) {
        return eventReviewRepository.findById(id)
                .map(e -> {
                    EventReview eventReview = new EventReview();
                    eventReview.setReviewStatus(status);
                    return new EventReviewStatusDto(id, status);
                })
                .orElse(null);
    }

    public EventReviewCommentDto updateComment(Long id, String comment) {
        return eventReviewRepository.findById(id)
                .map(e -> {
                    EventReview eventReview = new EventReview();
                    eventReview.setComment(comment);
                    return new EventReviewCommentDto(id, comment);
                })
                .orElse(null);
    }
}
