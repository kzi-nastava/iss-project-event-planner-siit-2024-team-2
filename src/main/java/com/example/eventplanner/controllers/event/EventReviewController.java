package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.eventreview.*;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.services.event.EventReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/event-reviews")
@RequiredArgsConstructor()
public class EventReviewController {
    private final EventReviewService eventReviewService;

    @GetMapping
    public ResponseEntity<Collection<EventReviewDto>> getEventReviews() {
        Collection<EventReviewDto> result = eventReviewService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EventReviewDto> getEventReviewById(@PathVariable("id") Long id) {
        EventReviewDto result = eventReviewService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EventReviewDto> createEventReview(@RequestBody EventReviewNoIdDto dto) {
        EventReviewDto result = eventReviewService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventReviewDto> updateEventReview(@PathVariable("id") Long id, @RequestBody EventReviewNoIdDto dto) {
        EventReviewDto result = eventReviewService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EventReviewDto> deleteEventReview(@PathVariable("id") Long id) {
        boolean success = eventReviewService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}/status")
    public ResponseEntity<EventReviewStatusDto> updateEventReviewStatus(@PathVariable("id") Long id, @RequestBody ReviewStatus status) {
         EventReviewStatusDto result = eventReviewService.updateStatus(id, status);
         return result != null ?
                 new ResponseEntity<>(result, HttpStatus.OK) :
                 new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}/comment")
    public ResponseEntity<EventReviewCommentDto> updateEventReviewComment(@PathVariable("id") Long id, @RequestBody String comment) {
        EventReviewCommentDto result = eventReviewService.updateComment(id, comment);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
