package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    List<Order> orders = new ArrayList<>();
    public OrderService() {}
}
