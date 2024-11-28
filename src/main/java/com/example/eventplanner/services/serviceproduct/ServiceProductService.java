package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.user.ServiceProductProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
@Getter
@Setter
public class ServiceProductService {
    private final static AtomicLong counter = new AtomicLong();
    private final HashMap<Long, ServiceProduct> serviceProducts = new HashMap<>();


    public ServiceProductService() {}

    public Collection<ServiceProductSummaryDto> getTop5() {
        return serviceProducts.values()
                .stream()
                .filter(Entity::isActive)
                .filter(ServiceProduct::isVisible)
                .limit(5)
                .map(ServiceProductMapper::toSummaryDto)
                .toList();
    }

    public List<ServiceProductDto> getAll() {
        return serviceProducts.values()
                .stream()
                .filter(Entity::isActive)
                .map(ServiceProductMapper::toDto)
                .toList();
    }

    public ServiceProductDto getById(long id) {
        if (!serviceProducts.containsKey(id) || !serviceProducts.get(id).isActive())
            return null;
        return ServiceProductMapper.toDto(serviceProducts.get(id));
    }

    public ServiceProductDto create(ServiceProductNoIdDto dto) {
        ServiceProduct serviceProduct = ServiceProductMapper.toEntity(dto);
        serviceProduct.setId(counter.getAndIncrement());
        serviceProduct.setActive(true);

        // link category
        ServiceProductCategory testCategory = new ServiceProductCategory();
        testCategory.setId(dto.getCategoryId());
        serviceProduct.setCategory(testCategory);
        // link available event types
        serviceProduct.setAvailableEventTypes(new ArrayList<>());
        // link service provider
        ServiceProductProvider testSPP = new ServiceProductProvider();
        testSPP.setId(dto.getServiceProductProviderId());
        serviceProduct.setServiceProductProvider(testSPP);

        serviceProducts.put(serviceProduct.getId(), serviceProduct);
        return ServiceProductMapper.toDto(serviceProduct);
    }

    public boolean delete(long id) {
        if (this.getById(id) == null)
            return false;
        serviceProducts.get(id).setActive(false);
        return true;
    }

    public Collection<ServiceProductDto> getAllFilteredPaginated(
            int page, Integer size, String name, String description, List<Long> categoryIds, 
            Boolean available, Boolean visible, Integer minPrice, Integer maxPrice, 
            List<Long> availableEventTypeIds, Long serviceProductProviderId) {
        Stream<ServiceProduct> filtered = serviceProducts.values().stream()
                .filter(Entity::isActive)
                .filter(serviceProduct -> name == null || name.isEmpty() || serviceProduct.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(serviceProduct -> description == null || description.isEmpty()
                        || serviceProduct.getDescription().toLowerCase().contains(description.toLowerCase()))
                .filter(serviceProduct -> categoryIds == null || categoryIds.contains(serviceProduct.getCategory().getId()))
                .filter(serviceProduct -> available == null || serviceProduct.isAvailable() == available)
                .filter(serviceProduct -> visible == null || serviceProduct.isVisible() == visible)
                .filter(serviceProduct -> minPrice == null || serviceProduct.getPrice() >= minPrice)
                .filter(serviceProduct -> maxPrice == null || serviceProduct.getPrice() <= maxPrice)
                .filter(serviceProduct -> availableEventTypeIds == null ||
                        serviceProduct.getAvailableEventTypes().stream().map(Entity::getId).noneMatch(availableEventTypeIds::contains))
                .filter(serviceProduct -> serviceProductProviderId == null ||
                        serviceProductProviderId.equals(serviceProduct.getServiceProductProvider().getId()));
        if (size != null && size > 0)
            filtered = filtered.skip((long) page * size).limit(size);
        return filtered.map(ServiceProductMapper::toDto).toList();
    }
}
