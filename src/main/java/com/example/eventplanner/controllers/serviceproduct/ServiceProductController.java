package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.services.serviceproduct.ServiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-products")
@RequiredArgsConstructor()
public class ServiceProductController {
    private final ServiceProductService serviceProductService;
}
