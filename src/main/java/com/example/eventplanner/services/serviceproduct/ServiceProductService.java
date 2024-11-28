package com.example.eventplanner.services.serviceproduct;


import com.example.eventplanner.dto.serviceproduct.product.ProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductMapper;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Getter
@Setter
public class ServiceProductService {

    private final static AtomicLong counter = new AtomicLong();
    private final HashMap<Long, ServiceProduct> serviceProducts = new HashMap<>();

    public ServiceProductService() {}

    public List<ServiceProduct> getTop5() {
        return serviceProducts.values()
                .stream()
                .limit(5)
                .toList();
    }


    // it could be used DTO, but getAll isn't necessary now
    // you should set isActive attribute
    public Collection<ServiceProduct> getAll() {
        return serviceProducts.values().stream().toList();
    }

    public ServiceProduct getDetailsById(Long id) {
        ServiceProduct serviceProduct = serviceProducts.get(id);
        if (serviceProduct != null)
            return serviceProduct;
        return null;
    }

    public ServiceProduct create(ServiceProduct serviceProduct) {
        long id = counter.incrementAndGet();
        serviceProduct.setId(id);
        serviceProducts.put(id, serviceProduct);
        return serviceProduct;
    }
}
