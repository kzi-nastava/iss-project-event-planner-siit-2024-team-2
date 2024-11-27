package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.ServiceDto;
import com.example.eventplanner.services.serviceproduct.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor()
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping()
    public ResponseEntity<Collection<ServiceDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAll());
    }

    @GetMapping(value = "{id}")
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
}
