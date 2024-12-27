package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.service.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import lombok.RequiredArgsConstructor;
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

        if (serviceDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(serviceDto);
    }

    @PostMapping()
    public ResponseEntity<ServiceDto> createService(@RequestBody CreateServiceDto ServiceDto) {
        return ResponseEntity.ok(serviceService.create(ServiceDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ServiceDto> updateService(@RequestBody CreateServiceDto serviceDto, @PathVariable("id") Long id) {
        ServiceDto updatedServiceDto = serviceService.update(id, serviceDto);
        if (updatedServiceDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedServiceDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ServiceDto> deleteService(@PathVariable("id") Long id) {
        boolean success = serviceService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ServiceDto>> searchServicesByName(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(required = false) Integer size,
                                                                       @RequestParam(required = false) String name) {
        Collection<ServiceDto> serviceDtos = serviceService.searchByName(page, size, name);
        if (serviceDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
        return ResponseEntity.ok(serviceDtos);
    }

    @GetMapping("/filter")
    public ResponseEntity<Collection<ServiceDto>> filterServices(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(required = false) Integer size,
                                                                 @RequestParam(required = false) List<String> categories,
                                                                 @RequestParam(required = false) Float minPrice,
                                                                 @RequestParam(required = false) Float maxPrice,
                                                                 @RequestParam(required = false) Boolean available) {
//                                                                 @RequestParam(value = "eventTypes", required = false) List<String> eventTypes,
        return ResponseEntity.ok(serviceService.filter(page, size, categories, minPrice, maxPrice, available));
    }
}
