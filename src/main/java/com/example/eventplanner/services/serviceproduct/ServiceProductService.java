package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.dto.order.review.ReviewSummaryDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductFilteringValuesDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.dto.order.review.ReviewDto;
import com.example.eventplanner.dto.order.review.ReviewMapper;
import com.example.eventplanner.exception.NotFoundException;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.model.utils.ServiceProductDType;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.repositories.order.ServiceProductReviewRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductCategoryRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ServiceProductService {
    private final ServiceProductRepository serviceProductRepository;
    private final ServiceProductCategoryRepository serviceProductCategoryRepository;
    private final EventTypeRepository eventTypeRepository;
    private final ServiceProductReviewRepository serviceProductReviewRepository;

    public Collection<ServiceProductSummaryDto> getTop5() {
        return serviceProductRepository.findTop5()
                .stream()
                .map(ServiceProductMapper::toSummaryDto)
                .toList();
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

    @Transactional
    public boolean delete(long id) {
        if (!serviceProductRepository.existsById(id))
            throw new NotFoundException("Service product not found");
        serviceProductReviewRepository.deleteByServiceProduct(id);
        serviceProductRepository.deleteById(id);
        return true;
    }

    public <T> Page<T> getAllFiltered(
            Class<T> clazz, ServiceProductDType type,
            int page, Integer size, Sort sort, String name, String description, List<Long> categoryIds,
            Boolean available, Integer minPrice, Integer maxPrice,
            List<Long> availableEventTypeIds, Long serviceProductProviderId,
            Float minDuration, Float maxDuration, Boolean automaticReserved) {
        PageRequest pageRequest;
        if (sort == null) {
            pageRequest = PageRequest.of(page, size != null ? size : 10);
        }
        else pageRequest = PageRequest.of(page, size != null ? size : 10, sort);
        Class<?> spType;
        if (type == ServiceProductDType.SERVICE)
            spType = com.example.eventplanner.model.serviceproduct.Service.class;
        else if (type == ServiceProductDType.PRODUCT)
            spType = Product.class;
        else
            spType = null;
        Page<ServiceProduct> serviceProducts =
                serviceProductRepository.findAllFiltered(spType, name, description, categoryIds, available,
                        minPrice, maxPrice, availableEventTypeIds, serviceProductProviderId,
                        minDuration, maxDuration, automaticReserved, pageRequest);
        if (clazz == ServiceProductDto.class)
            return serviceProducts.map(ServiceProductMapper::toDto).map(clazz::cast);
        else
            return serviceProducts.map(ServiceProductMapper::toSummaryDto)
                .map(clazz::cast);
    }

    public ServiceProductFilteringValuesDto getFilteringValues() {
        List<Object[]> result = serviceProductRepository.findPriceRange();
        Double minPrice = (Double) result.get(0)[0];
        Double maxPrice = (Double) result.get(0)[1];

        result = serviceProductRepository.findDurationRange();
        Float minDuration = (Float) result.get(0)[0];
        Float maxDuration = (Float) result.get(0)[1];

        List<ServiceProductCategoryDto> categories = serviceProductCategoryRepository.findAll()
                .stream().map(ServiceProductCategoryMapper::toDto).toList();
        List<EventTypeDto> types = eventTypeRepository.findAll()
                .stream().map(EventTypeMapper::toDto).toList();

        return new ServiceProductFilteringValuesDto(
                minPrice,
                maxPrice,
                minDuration,
                maxDuration,
                categories,
                types
        );
    }

    public List<String> getCategoriesByAvailableEventType(Long eventTypeId) {
        EventType eventType = eventTypeRepository.getReferenceById(eventTypeId);
        return serviceProductRepository.getCategoriesByAvailableEventType((eventType));
    }

    public Page<ReviewSummaryDto> getServiceProductReviews(Long id, Pageable pageable) {
        ServiceProduct serviceProduct = serviceProductRepository.getReferenceById(id);
        return serviceProductReviewRepository.findAllByServiceProductAndReviewStatus(serviceProduct, ReviewStatus.APPROVED, pageable)
                .map(ReviewMapper::toSummaryDto);
    }
}
