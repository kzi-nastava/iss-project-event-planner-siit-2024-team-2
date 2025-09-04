package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.serviceproduct.ServiceProductNameIdDto;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.services.serviceproduct.ImageService;

import java.util.List;

public class ServiceProductMapper {
    private ServiceProductMapper() {}

    public static ServiceProductDto toDto(ServiceProduct serviceProduct) {
        if (serviceProduct == null)
            return null;

        return new ServiceProductDto(
                serviceProduct.getId(),
                ServiceProductCategoryMapper.toDto(serviceProduct.getCategory()),
                serviceProduct.isAvailable(),
                serviceProduct.isVisible(),
                serviceProduct.getPrice(),
                serviceProduct.getDiscount(),
                serviceProduct.getName(),
                serviceProduct.getDescription(),
                serviceProduct.getImages().stream().map(ImageService::encodePath).toList(),
                serviceProduct.getAvailableEventTypes().stream().map(EventTypeMapper::toDto).toList(),
                UserMapper.toServiceProductProviderDto(serviceProduct.getServiceProductProvider()),
                serviceProduct.getDtype()
        );
    }

    public static ServiceProductSummaryDto toSummaryDto(ServiceProduct serviceProduct) {
        if (serviceProduct == null)
            return null;
        ServiceProductProvider provider = serviceProduct.getServiceProductProvider();

        return new ServiceProductSummaryDto(
                serviceProduct.getId(),
                ServiceProductCategoryMapper.toDto(serviceProduct.getCategory()),
                serviceProduct.isAvailable(),
                serviceProduct.getPrice(),
                serviceProduct.getDiscount(),
                serviceProduct.getName(),
                serviceProduct.getDescription(),
                provider != null ? provider.getCompanyName() : null,
                provider != null ? provider.getEmail() : null,
                ImageService.encodePath(provider != null ? provider.getImage() : null),
                ImageService.encodePath(
                        serviceProduct.getImages()
                                .stream()
                                .sorted()
                                .findFirst()
                                .orElse(null))
        );
    }

    public static ServiceProduct toEntity(ServiceProductNoIdDto dto,
                                          ServiceProductCategory category,
                                          List<EventType> availableEventTypes,
                                          ServiceProductProvider serviceProductProvider) {
        if (dto == null)
            return null;

        return new ServiceProduct(
                category,
                dto.isAvailable(),
                dto.isVisible(),
                dto.getPrice(),
                dto.getDiscount(),
                dto.getName(),
                dto.getDescription(),
                dto.getImages().stream().map(ImageService::decodePath).toList(),
                availableEventTypes,
                serviceProductProvider);
    }

    public static PriceListDto toPriceListItemDto(ServiceProduct sp) {
        if (sp == null)
            return null;
        return new PriceListDto(
                sp.getId(),
                sp.getName(),
                sp.getPrice(),
                sp.getDiscount()
       );
    }

    public static ServiceProductNameIdDto toNameDto(ServiceProduct serviceProduct) {
        if (serviceProduct == null)
            return null;

        return new ServiceProductNameIdDto(
                serviceProduct.getId(),
                serviceProduct.getName(),
                serviceProduct.getDescription()
        );
    }
}