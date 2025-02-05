package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventCreatorProjection;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.serviceproduct.ServiceProductCreatorProjection;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductCategoryRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.repositories.user.ServiceProductProviderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ServiceProductService {
    private final ServiceProductRepository serviceProductRepository;
    private final ServiceProductCategoryRepository serviceProductCategoryRepository;
    private final ServiceProductProviderRepository serviceProductProviderRepository;
    private final EventTypeRepository eventTypeRepository;

    public Collection<ServiceProductSummaryDto> getTop5() {
        List<ServiceProduct> serviceProducts =  serviceProductRepository.findTop5();
        return constructServiceProductSummaries(serviceProducts);
    }

    public List<ServiceProductDto> getAll() {
        return serviceProductRepository.findAll()
                .stream()
                .map(ServiceProductMapper::toDto)
                .toList();
    }

    public ServiceProductDto getById(long id) {
        return serviceProductRepository.findById(id)
                .map(ServiceProductMapper::toDto)
                .orElse(null);
    }

    public boolean delete(long id) {
        if (!serviceProductRepository.existsById(id))
            return false;
        serviceProductRepository.deleteById(id);
        return true;
    }

    public <T> Page<T> getAllFiltered(
            Class<T> clazz, int page, Integer size, Sort sort, String name, String description, List<Long> categoryIds,
            Boolean available, Boolean visible, Integer minPrice, Integer maxPrice,
            List<Long> availableEventTypeIds, Long serviceProductProviderId) {
        PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10, sort);
        Page<ServiceProduct> serviceProducts =
                serviceProductRepository.findAllFiltered(name, description,
                        categoryIds,
                        available,
                visible, minPrice, maxPrice,
                        availableEventTypeIds,
                        serviceProductProviderId, pageRequest);
        if (clazz == ServiceProductDto.class)
            return serviceProducts.map(ServiceProductMapper::toDto).map(clazz::cast);
        else
            return constructServiceProductSummaries(serviceProducts).map(clazz::cast);
    }

    private Page<ServiceProductSummaryDto> constructServiceProductSummaries(Page<ServiceProduct> serviceProducts) {
        Map<Long, ServiceProductCreatorProjection> creatorMap = constructServiceProductCreatorMap(serviceProducts.toList());
        return serviceProducts.map(serviceProduct -> {
            ServiceProductCreatorProjection serviceProductCreator = creatorMap.get(serviceProduct.getId());
            return ServiceProductMapper.toSummaryDto(serviceProduct, serviceProductCreator.getCreatorUsername(), serviceProductCreator.getCreatorEmail());
        });
    }

    private List<ServiceProductSummaryDto> constructServiceProductSummaries(List<ServiceProduct> serviceProducts) {
        Map<Long, ServiceProductCreatorProjection> creatorMap = constructServiceProductCreatorMap(serviceProducts);
        return serviceProducts.stream().map(serviceProduct -> {
            ServiceProductCreatorProjection serviceProductCreator = creatorMap.get(serviceProduct.getId());
            return ServiceProductMapper.toSummaryDto(serviceProduct, serviceProductCreator.getCreatorUsername(), serviceProductCreator.getCreatorEmail());
        }).toList();
    }

    private Map<Long, ServiceProductCreatorProjection> constructServiceProductCreatorMap(Collection<ServiceProduct> serviceProducts) {
        List<Long> serviceProductIds = serviceProducts.stream().map(ServiceProduct::getId).toList();
        List<ServiceProductCreatorProjection> serviceProductCreators = serviceProductProviderRepository.findCreatorsByServiceProductIds(serviceProductIds);
        return serviceProductCreators.stream()
                .collect(Collectors.toMap(ServiceProductCreatorProjection::getServiceProductId, Function.identity()));
    }
}
