package com.example.eventplanner.dto.serviceproduct.service;

import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.dto.user.user.ServiceProductProviderMapper;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.user.ServiceProductProvider;

import java.util.List;

public class ServiceMapper {
    public static ServiceDto toDto(Service service) {
        if (service == null) {
            return null;
        }
        return new ServiceDto(
                service.getId(),
                ServiceProductCategoryMapper.toDto(service.getCategory()),
                service.isAvailable(), service.isVisible(),
                service.getPrice(), service.getDiscount(),
                service.getName(), service.getDescription(), service.getImages(),
                service.getAvailableEventTypes().stream().map(EventTypeMapper::toDto).toList(),
                ServiceProductProviderMapper.toDto(service.getServiceProductProvider()),
                service.getSpecifies(), service.getDuration(),
                service.getMinEngagementDuration(), service.getMaxEngagementDuration(),
                service.getReservationDaysDeadline(), service.getCancellationDaysDeadline(),
                service.isAutomaticReserved());
    }

    public static Service toEntity(CreateServiceDto dto,
                                   List<EventType> availableEventTypes,
                                   ServiceProductCategory serviceProductCategory,
                                   ServiceProductProvider serviceProductProvider) {
        if (dto == null) {
            return null;
        }
        Service service = new Service();
        if (serviceProductCategory != null)
            service.setCategory(serviceProductCategory);

        if (serviceProductProvider != null)
            service.setServiceProductProvider(serviceProductProvider);

        if (availableEventTypes != null || !availableEventTypes.isEmpty())
            service.setAvailableEventTypes(availableEventTypes);

        setOtherServiceAttributes(dto, service);

        return service;
    }

    public static void setOtherServiceAttributes(CreateServiceDto dto, Service service) {
        service.setAvailable(dto.isAvailable());
        service.setVisible(dto.isVisible());
        service.setPrice(dto.getPrice());
        service.setDiscount(dto.getDiscount());
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setImages(dto.getImages());

        service.setSpecifies(dto.getSpecifies());
        service.setDuration(dto.getDuration());
        service.setMinEngagementDuration(dto.getMinEngagementDuration());
        service.setMaxEngagementDuration(dto.getMaxEngagementDuration());
        service.setReservationDaysDeadline(dto.getReservationDaysDeadline());
        service.setCancellationDaysDeadline(dto.getCancellationDaysDeadline());
        service.setAutomaticReserved(dto.isAutomaticReserved());
    }
}
