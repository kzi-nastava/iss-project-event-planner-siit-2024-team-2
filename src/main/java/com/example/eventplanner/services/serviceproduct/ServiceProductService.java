package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceProductService {
    List<ServiceProduct> serviceProducts = new ArrayList<>();
    public ServiceProductService() {}
}
