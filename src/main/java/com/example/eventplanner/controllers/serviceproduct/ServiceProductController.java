package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.services.serviceproduct.ServiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import java.util.List;

@RestController
@RequestMapping("/api/service-products")
@RequiredArgsConstructor()
public class ServiceProductController {
    private final ServiceProductService serviceProductService;

    @GetMapping(value = "/top5")
    public ResponseEntity<List<ServiceProduct>> getTop5() {
        List<ServiceProduct> top5 = serviceProductService.getTop5();
        return new ResponseEntity<>(top5, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ServiceProduct> createServiceProduct(@RequestBody ServiceProduct serviceProduct) {
        return ResponseEntity.ok(serviceProductService.create(serviceProduct));
    }

    @GetMapping()
    public ResponseEntity<Collection<ServiceProduct>> getAllServiceProduct() {
        return ResponseEntity.ok(serviceProductService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ServiceProduct> getServiceProductDetailsById(@PathVariable("id") Long id) {
        ServiceProduct serviceProduct = serviceProductService.getDetailsById(id);

        if (serviceProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(serviceProduct);
    }
}
