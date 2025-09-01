package com.example.eventplanner.controllers.order;

import com.example.eventplanner.dto.order.review.ReviewCommentDto;
import com.example.eventplanner.dto.order.review.ReviewDto;
import com.example.eventplanner.dto.order.review.ReviewNoIdDto;
import com.example.eventplanner.dto.order.review.ReviewStatusDto;
import com.example.eventplanner.services.order.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor()
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Collection<ReviewDto>> getAllReviews() {
        Collection<ReviewDto> result = reviewService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<ReviewDto>> getAllPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        Pageable pageable = PageRequest.of(page, size != null ? size : 10)
                .withSort(Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewDto> result = reviewService.getAllPending(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("id") Long id) {
        ReviewDto result = reviewService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewNoIdDto dto) {
        ReviewDto result = reviewService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("id") Long id, @RequestBody ReviewNoIdDto dto) {
        ReviewDto result = reviewService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") Long id) {
        boolean success = reviewService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/approve")
    public ResponseEntity<ReviewStatusDto> approveReview(@RequestBody Long id) {
         ReviewStatusDto result = reviewService.approve(id);
         return result != null ?
                 new ResponseEntity<>(result, HttpStatus.OK) :
                 new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}/comment")
    public ResponseEntity<ReviewCommentDto> updateReviewComment(@PathVariable("id") Long id, @RequestBody String comment) {
        ReviewCommentDto result = reviewService.updateComment(id, comment);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
