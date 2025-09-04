package com.example.eventplanner.services.serviceproduct;

import java.util.*;
import com.example.eventplanner.dto.serviceproduct.product.CreateProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductDetailsDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductMapper;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.repositories.serviceproduct.ProductRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductCategoryRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final EventTypeRepository eventTypeRepository;
    private final ServiceProductCategoryRepository serviceProductCategoryRepository;
    private final UserRepository userRepository;



    public Collection<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    public ProductDetailsDto getById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null)
            return ProductMapper.toDetailsDto(product);
        return null;
    }

    public ProductDto create(CreateProductDto createProductDto) {
        ServiceProductProvider serviceProductProvider = (ServiceProductProvider) userRepository.findById(createProductDto.getServiceProductProviderId()).orElseThrow();
        ServiceProductCategory serviceProductCategory = serviceProductCategoryRepository.findById(createProductDto.getCategoryId()).orElseThrow();
        List<EventType> eventTypes = new ArrayList<>();
        for (long typeId: createProductDto.getAvailableEventTypeIds()) {
            EventType eventType = eventTypeRepository.findById(typeId).orElseThrow(() ->
                    new NoSuchElementException("EventType with ID " + typeId + " not found"));
            eventTypes.add(eventType);
        }
        Product product = ProductMapper.toEntity(createProductDto, serviceProductProvider, serviceProductCategory, eventTypes);
        product.setActive(true);
        productRepository.save(product);
        return ProductMapper.toDto(product);
    }

    public ProductDto update(Long id, CreateProductDto createProductDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        ServiceProductProvider serviceProductProvider = (ServiceProductProvider) userRepository.findById(createProductDto.getServiceProductProviderId()).orElseThrow();
        ServiceProductCategory serviceProductCategory = serviceProductCategoryRepository.findById(createProductDto.getCategoryId()).orElseThrow();
        List<EventType> eventTypes = new ArrayList<>();
        for (long typeId: createProductDto.getAvailableEventTypeIds()) {
            EventType eventType = eventTypeRepository.findById(typeId)
                    .orElseThrow(() -> new NoSuchElementException("EventType with ID " + typeId + " not found"));
            eventTypes.add(eventType);
        }
        product.setServiceProductProvider(serviceProductProvider);
        product.setCategory(serviceProductCategory);
        product.setAvailableEventTypes(eventTypes);
        product.setVisible(createProductDto.isVisible());
        product.setName(createProductDto.getName());
        product.setPrice(createProductDto.getPrice());
        product.setAvailable(createProductDto.isAvailable());
        product.setDescription(createProductDto.getDescription());
        product.setActive(true);
        productRepository.save(product);
        return ProductMapper.toDto(product);
    }

    public boolean delete(Long id) {
        if (!productRepository.existsById(id))
            return false;
        productRepository.deleteById(id);
        return true;
    }

    public Collection<ProductDto> searchByName(String name) {
        return this.getAll().stream()
                .filter(product -> product.getName() != null &&
                        product.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public List<ProductDto> filter(Long sppId, Long categoryId, List<Long> eventTypeIds, Float minPrice, Float maxPrice, Boolean available) {
        //TODO optimize this
        return productRepository.findByServiceProductProviderId(sppId).stream()
                .filter(product -> categoryId == null || categoryId == product.getCategory().getId())
                .filter(product -> eventTypeIds == null || eventTypeIds.isEmpty() || product.getAvailableEventTypes().stream().map(EventType::getId).anyMatch(eventTypeIds::contains))
                .filter(product -> available == null || available == product.isAvailable())
                .filter(product -> (minPrice == null || minPrice <= product.getPrice()) && (maxPrice == null || maxPrice >= product.getPrice() || maxPrice == 0))
                .map(ProductMapper::toDto).toList();
    }

    public List<ProductDto> getAllByProviderId(long id) {
        return productRepository
                .findByServiceProductProviderId(id)
                .stream()
                .map(ProductMapper::toDto)
                .toList();
    }
}