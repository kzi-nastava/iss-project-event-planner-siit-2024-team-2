package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.dto.user.serviceProduct.ServiceProductDto;
import com.example.eventplanner.dto.user.serviceProduct.ServiceProductMapper;
import com.example.eventplanner.dto.user.serviceProduct.ServiceProductNoIdDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.user.ServiceProduct;
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

    public Collection<ServiceProductSummaryDto> getTop5() {
        return serviceProducts.values()
                .stream()
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
}
