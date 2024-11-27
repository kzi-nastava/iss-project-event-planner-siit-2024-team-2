package com.example.eventplanner.controllers.event;

import com.example.eventplanner.services.event.SPOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor()
public class SPOrderController {
    private final SPOrderService spOrderService;
}
