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
        BaseUser user = userRepository.getReferenceById(dto.getUserId());
        Event event = eventRepository.getReferenceById(dto.getEventId());

        EventReview eventReview = EventReviewMapper.toEntity(dto, user, event);
        eventReviewRepository.save(eventReview);
        return EventReviewMapper.toDto(eventReview);
    }

    public EventReviewDto update(EventReviewNoIdDto dto, long id) {
        return eventReviewRepository.findById(id)
                .map(er -> {
                    er.setReviewStatus(dto.getReviewStatus());
                    er.setComment(dto.getComment());
                    er.setGrade(dto.getGrade());
                    BaseUser user = userRepository.getReferenceById(dto.getUserId());
                    Event event = eventRepository.getReferenceById(dto.getEventId());
                    er.setUser(user);
                    er.setEvent(event);
                    return EventReviewMapper.toDto(eventReviewRepository.save(er));
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        if (!eventReviewRepository.existsById(id))
            return false;
        eventReviewRepository.deleteById(id);
        return true;
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
