package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.service.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor()
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping()
    public ResponseEntity<Collection<ServiceDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable("id") Long id) {
        ServiceDto serviceDto = serviceService.getById(id);

        return serviceDto != null ?
                ResponseEntity.ok(serviceDto) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<ServiceDto> createService(@RequestBody CreateServiceDto ServiceDto) {
        return new ResponseEntity<>(serviceService.create(ServiceDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ServiceDto> updateService(@RequestBody CreateServiceDto serviceDto, @PathVariable("id") Long id) {
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

    @GetMapping("/search")
    public ResponseEntity<Page<ServiceDto>> searchServicesByName(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(required = false) Integer size,
                                                                       @RequestParam(required = false) String name) {
        Page<ServiceDto> serviceDtos = serviceService.searchByName(page, size, name);
        return ResponseEntity.ok(serviceDtos);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ServiceDto>> filterServices(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(required = false) Integer size,
                                                                 @RequestParam(required = false) List<String> categories,
                                                                 @RequestParam(required = false) Float minPrice,
                                                                 @RequestParam(required = false) Float maxPrice,
                                                                 @RequestParam(required = false) Boolean available,
                                                                 @RequestParam(required = false) List<Long> availableEventTypeIds) {
        return ResponseEntity.ok(serviceService.filter(page, size, minPrice, maxPrice, available, categories, availableEventTypeIds));
    }
}
