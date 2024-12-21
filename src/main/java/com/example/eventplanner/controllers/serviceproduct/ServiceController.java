package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.service.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor()
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
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
                                                                 @RequestParam(required = false) boolean available) {
//                                                                 @RequestParam(value = "eventTypes", required = false) List<String> eventTypes,
        return ResponseEntity.ok(serviceService.filter(page, size, categories, minPrice, maxPrice, available));
    }
}
