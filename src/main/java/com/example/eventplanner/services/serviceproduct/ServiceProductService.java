package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.product.ProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Product;
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
    private long idCounter = 0;
    public ServiceProductService() {}

    public List<ServiceProduct> getTop5() {
        return serviceProducts.values()
                .stream()
                .limit(5)
                .toList();
    }

    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.toEntity(productDto);
        serviceProducts.put(++idCounter, product);
        return ProductMapper.toDto(product);
    }
    public ProductDto updateProduct(long id, ProductDto productDto) {
        Product product = (Product) serviceProducts.get(id);
        if (product == null || !product.isActive())
            return null;
        product.setName(product.getName());
        product.setAvailable(product.isAvailable());
        product.setDescription(product.getDescription());
        product.setPrice(product.getPrice());

        return ProductMapper.toDto(product);
    }
}
