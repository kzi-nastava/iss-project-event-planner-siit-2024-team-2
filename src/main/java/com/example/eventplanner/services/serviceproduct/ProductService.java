package com.example.eventplanner.services.serviceproduct;

import java.util.*;
import com.example.eventplanner.dto.serviceproduct.product.CreateProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductMapper;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private long idCounter = 0;
    private final HashMap<Long, Product> products = new HashMap<>();

    public Collection<ProductDto> getAll() {
        return products.values().stream().filter(Entity::isActive).map(ProductMapper::toDto).toList();
    }

    public ProductDto getById(Long id) {
        Product product = products.get(id);
        if (product != null && product.isActive())
            return ProductMapper.toDto(product);
        return null;
    }

    public ProductDto create(CreateProductDto createProductDto) {
        Product product = ProductMapper.toEntity(createProductDto);
        product.setId(++idCounter);
        product.setActive(true);
        products.put(product.getId(), product);
        return ProductMapper.toDto(product);
    }

    public ProductDto update(Long id, CreateProductDto createProductDto) {
        if (this.getById(id) == null) {
            return null;
        }
        Product product = ProductMapper.toEntity(getById(id));
        product.setName(createProductDto.getName());
        product.setPrice(createProductDto.getPrice());
        product.setAvailable(createProductDto.isAvailable());
        product.setDescription(createProductDto.getDescription());
        product.setActive(true);
        return ProductMapper.toDto(product);
    }

    public boolean delete(Long id) {
        if (this.getById(id) == null) {
            return false;
        }
        products.get(id).setActive(false);
        return true;
    }

    public Collection<ProductDto> searchByName(String name) {
        return this.getAll().stream()
                .filter(product -> product.getName() != null &&
                        product.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public Collection<ProductDto> filter(List<ServiceProductCategory> categories, List<String> eventTypes, Float minPrice, Float maxPrice, Boolean available) {
        List<String> categoryNames = new ArrayList<>();
        if (categories != null) {
            for (ServiceProductCategory category : categories)
                categoryNames.add(category.getName());
        }

        return products.values().stream()
                .filter(product -> categoryNames.isEmpty() || categoryNames.contains(product.getCategory().getName()))
                .filter(product -> eventTypes == null || product.getAvailableEventTypes().stream().map(EventType::getName).anyMatch(eventTypes::contains))
                .filter(product -> available == null || available == product.isAvailable())
                .filter(product -> (minPrice == null || minPrice <= product.getPrice()) && (maxPrice == null || maxPrice >= product.getPrice()))
                .map(ProductMapper::toDto).toList();
    }
}