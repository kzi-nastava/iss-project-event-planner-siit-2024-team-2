package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.serviceproduct.service.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceCardDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.dto.util.DateRangeDto;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.ServiceProductDType;
import com.example.eventplanner.services.order.BookingService;
import com.example.eventplanner.services.serviceproduct.ServiceProductService;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor()
@Validated
public class ServiceController {
    private final ServiceService serviceService;
    private final ServiceProductService serviceProductService;
    private final AuthUtil authUtil;

    @GetMapping("/all")
    public ResponseEntity<Page<ServiceDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(required = false) Integer size,
                                                   @RequestParam(defaultValue = "") String name,
                                                   @RequestParam(required = false) List<String> categories,
                                                   @RequestParam(required = false) Float minPrice,
                                                   @RequestParam(required = false) Float maxPrice,
                                                   @RequestParam(required = false) Boolean available,
                                                   @RequestParam(required = false) List<Long> availableEventTypeIds) {
        return ResponseEntity.ok(serviceService.filter(page, size, name, minPrice, maxPrice, available, categories, availableEventTypeIds));
    }

    @GetMapping()
    public ResponseEntity<Page<ServiceProductSummaryDto>> getMyServiceCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<Long> availableEventTypeIds) {
        long serviceProductProviderId = authUtil.getAuthenticatedUserId();
        Page<ServiceProductSummaryDto> result = serviceProductService.getAllFiltered(
                ServiceProductSummaryDto.class, ServiceProductDType.SERVICE,
                page, size, null, name, "", categoryIds, available,
                minPrice, maxPrice, availableEventTypeIds, serviceProductProviderId,
                null, null, null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<Collection<ServiceCardDto>> getAllBySPP_Id() {
        ServiceProductProvider provider = authUtil.getAuthenticatedServiceProductProvider();
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(serviceService.getAllBySPP_Id(provider.getId()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable("id") Long id) {
        ServiceDto serviceDto = serviceService.getById(id);

        return serviceDto != null ?
                ResponseEntity.ok(serviceDto) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<ServiceDto> createService(@Valid @RequestBody CreateServiceDto serviceDto) {
        return new ResponseEntity<>(serviceService.create(serviceDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ServiceDto> updateService(@Valid @RequestBody CreateServiceDto serviceDto, @PathVariable("id") Long id) {
        ServiceDto updatedServiceDto = serviceService.update(id, serviceDto);
        return updatedServiceDto != null ?
                ResponseEntity.ok(updatedServiceDto) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ServiceDto> deleteService(@PathVariable("id") Long id) {
        boolean success = serviceService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<List<DateRangeDto>> getAvailableDates(@PathVariable("id") Long id, @RequestParam Long eventId) {
        return ResponseEntity.ok(serviceService.getAvailableDates(id, eventId));
    }
}
