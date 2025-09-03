package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.order.review.ReviewEligibilityDto;
import com.example.eventplanner.dto.order.review.ReviewSummaryDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductFilteringValuesDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.dto.order.review.ReviewDto;
import com.example.eventplanner.model.utils.ServiceProductDType;
import com.example.eventplanner.services.order.ReviewService;
import com.example.eventplanner.services.serviceproduct.ServiceProductService;
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
import java.util.List;

@RestController
@RequestMapping("/api/service-products")
@RequiredArgsConstructor()
@Validated
public class ServiceProductController {
    private final ServiceProductService serviceProductService;
    private final ReviewService reviewService;

    @GetMapping(value = "/top5")
    public ResponseEntity<Collection<ServiceProductSummaryDto>> getTop5() {
        Collection<ServiceProductSummaryDto> result = serviceProductService.getTop5();
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<Page<ServiceProductDto>> getAllServiceProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) ServiceProductDType type,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<Long> availableEventTypeIds,
            @RequestParam(required = false) Long serviceProductProviderId,
            @RequestParam(required = false) Float minDuration,
            @RequestParam(required = false) Float maxDuration,
            @RequestParam(required = false) Boolean automaticReserved) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<ServiceProductDto> result = serviceProductService.getAllFiltered(
                ServiceProductDto.class, type,
                page, size, sort, name, description, categoryIds, available,
                minPrice, maxPrice, availableEventTypeIds, serviceProductProviderId,
                minDuration, maxDuration, automaticReserved);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/summaries")
    public ResponseEntity<Page<ServiceProductSummaryDto>> getServiceProductSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) ServiceProductDType type,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<Long> availableEventTypeIds,
            @RequestParam(required = false) Long serviceProductProviderId,
            @RequestParam(required = false) Float minDuration,
            @RequestParam(required = false) Float maxDuration,
            @RequestParam(required = false) Boolean automaticReserved) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<ServiceProductSummaryDto> result = serviceProductService.getAllFiltered(
                ServiceProductSummaryDto.class, type,
                page, size, sort, name, description, categoryIds, available,
                minPrice, maxPrice, availableEventTypeIds, serviceProductProviderId,
                minDuration, maxDuration, automaticReserved);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ServiceProductDto> getServiceProductById(@PathVariable("id") Long id) {
        ServiceProductDto result = serviceProductService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ServiceProductDto> deleteServiceProduct(@PathVariable("id") Long id) {
        boolean success = serviceProductService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/filtering-values")
    public ResponseEntity<ServiceProductFilteringValuesDto> getFilteringValues() {
        ServiceProductFilteringValuesDto result = serviceProductService.getFilteringValues();
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sp-categories/by-event-type")
    public ResponseEntity<List<String>> getCategoriesByEventType(@RequestParam Long eventTypeId) {
        return ResponseEntity.ok(serviceProductService.getCategoriesByAvailableEventType(eventTypeId));
    }

    @GetMapping(value = "/{id}/reviews")
    public ResponseEntity<Page<ReviewSummaryDto>> getServiceProductReviews(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        Pageable pageable = PageRequest.of(page, size != null ? size : 10)
                .withSort(Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewSummaryDto> result = serviceProductService.getServiceProductReviews(id, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/review-eligibility")
    public ResponseEntity<ReviewEligibilityDto> getServiceProductReviewEligibility(@PathVariable("id") Long id) {
        ReviewEligibilityDto result = reviewService.canReviewServiceProduct(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
