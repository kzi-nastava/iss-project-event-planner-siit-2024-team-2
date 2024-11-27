package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
@Setter
public class ServiceProductService {
    Map<Long, ServiceProduct> serviceProducts = new HashMap<>();
    public ServiceProductService() {}

    public List<ServiceProduct> getTop5() {
        return serviceProducts.values()
                .stream()
                .limit(5)
                .toList();
    }
}
