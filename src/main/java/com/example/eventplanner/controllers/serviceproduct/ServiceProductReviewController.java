package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.event.eventreview.EventReviewCommentDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewCommentDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.ServiceProductReviewStatusDto;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.services.serviceproduct.ServiceProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/service-product-reviews")
@RequiredArgsConstructor()
public class ServiceProductReviewController {
    private final ServiceProductReviewService serviceProductReviewService;

    @GetMapping
    public ResponseEntity<Collection<ServiceProductReviewDto>> getServiceProductReviews() {
        Collection<ServiceProductReviewDto> result = serviceProductReviewService.getAll();
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
    public ResponseEntity<ServiceProductReviewDto> deleteServiceProductReview(@PathVariable("id") Long id) {
        boolean success = serviceProductReviewService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}/status")
    public ResponseEntity<ServiceProductReviewStatusDto> updateServiceProductReviewStatus(@PathVariable("id") Long id, @RequestBody ReviewStatus status) {
         ServiceProductReviewStatusDto result = serviceProductReviewService.updateStatus(id, status);
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
