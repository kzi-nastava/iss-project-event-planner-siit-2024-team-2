package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.services.serviceproduct.ServiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-products")
@RequiredArgsConstructor()
public class ServiceProductController {
    private final ServiceProductService serviceProductService;

    @GetMapping(value = "/top5")
    public ResponseEntity<Collection<ServiceProductSummaryDto>> getTop5() {
        Collection<ServiceProductSummaryDto> result = serviceProductService.getTop5();
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<Page<ServiceProductDto>> getServiceProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<Long> availableEventTypeIds,
            @RequestParam(required = false) Long serviceProductProviderId) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<ServiceProductDto> result = serviceProductService.getAllFiltered(
                ServiceProductDto.class,
                page, size, sort, name, description, categoryIds, available, visible,
                minPrice, maxPrice, availableEventTypeIds, serviceProductProviderId);
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
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<Long> availableEventTypeIds,
            @RequestParam(required = false) Long serviceProductProviderId) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<ServiceProductSummaryDto> result = serviceProductService.getAllFiltered(
                ServiceProductSummaryDto.class,
                page, size, sort, name, description, categoryIds, available, visible,
                minPrice, maxPrice, availableEventTypeIds, serviceProductProviderId);
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
}
