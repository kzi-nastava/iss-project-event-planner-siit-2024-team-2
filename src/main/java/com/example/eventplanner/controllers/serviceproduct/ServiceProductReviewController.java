package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewCommentDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewStatusDto;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.services.serviceproduct.ServiceProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor()
public class ServiceProductReviewController {
    private final ServiceProductReviewService serviceProductReviewService;

    @GetMapping
    public ResponseEntity<Collection<ServiceProductReviewDto>> getAllServiceProductReviews() {
        Collection<ServiceProductReviewDto> result = serviceProductReviewService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<ServiceProductReviewDto>> getAllPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        Pageable pageable = PageRequest.of(page, size != null ? size : 10)
                .withSort(Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ServiceProductReviewDto> result = serviceProductReviewService.getAllPending(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ServiceProductReviewDto> getServiceProductReviewById(@PathVariable("id") Long id) {
        ServiceProductReviewDto result = serviceProductReviewService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ServiceProductReviewDto> createServiceProductReview(@RequestBody ServiceProductReviewNoIdDto dto) {
        ServiceProductReviewDto result = serviceProductReviewService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ServiceProductReviewDto> updateServiceProductReview(@PathVariable("id") Long id, @RequestBody ServiceProductReviewNoIdDto dto) {
        ServiceProductReviewDto result = serviceProductReviewService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteServiceProductReview(@PathVariable("id") Long id) {
        boolean success = serviceProductReviewService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/approve")
    public ResponseEntity<ServiceProductReviewStatusDto> approveServiceProductReview(@RequestBody Long id) {
         ServiceProductReviewStatusDto result = serviceProductReviewService.approve(id);
         return result != null ?
                 new ResponseEntity<>(result, HttpStatus.OK) :
                 new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}/comment")
    public ResponseEntity<ServiceProductReviewCommentDto> updateServiceProductReviewComment(@PathVariable("id") Long id, @RequestBody String comment) {
        ServiceProductReviewCommentDto result = serviceProductReviewService.updateComment(id, comment);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
