package com.example.eventplanner.services.serviceproduct;

import java.util.*;

import com.example.eventplanner.dto.serviceproduct.service.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceMapper;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.repositories.event.EventTypeRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductCategoryRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import com.example.eventplanner.repositories.user.ServiceProductProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
	private final ServiceRepository serviceRepository;
	private final EventTypeRepository eventTypeRepository;
	private final ServiceProductCategoryRepository serviceProductCategoryRepository;
	private final ServiceProductProviderRepository sppRepository;


	public Collection<ServiceDto> getAll() {
		return serviceRepository.findAll()
				.stream()
				.map(ServiceMapper::toDto)
				.toList();
	}
	
	public ServiceDto getById(Long id) {
		return serviceRepository.findById(id)
				.map(ServiceMapper::toDto)
				.orElse(null);
	}
	
	public ServiceDto create(CreateServiceDto dto) {
		List<EventType> availableEventTypes = getEventTypes(dto);
		ServiceProductCategory category = serviceProductCategoryRepository.findById(dto.getCategoryId()).orElse(null);
		ServiceProductProvider spp = sppRepository.findById(dto.getServiceProductProviderId()).orElse(null);

		if (category == null || spp == null) {
			throw new IllegalArgumentException("Service creation failed: One or more required entities are null. " +
					"Check category, and service-product provider.");
		}

		Service service = ServiceMapper.toEntity(dto, availableEventTypes, category, spp);
		Service savedService = serviceRepository.save(service);
		return ServiceMapper.toDto(savedService);
	}

	public ServiceDto update(long id, CreateServiceDto dto) {
		return serviceRepository.findById(id)
				.map(service -> {
					service.setId(id);
					service.setActive(true);
					serviceProductCategoryRepository.findById(dto.getCategoryId()).ifPresent(service::setCategory);
					service.setAvailableEventTypes(getEventTypes(dto));
					sppRepository.findById(dto.getServiceProductProviderId()).ifPresent(service::setServiceProductProvider);
					ServiceMapper.setOtherServiceAttributes(dto, service);
					Service updatedService = serviceRepository.save(service);
					return ServiceMapper.toDto(updatedService);
				})
				.orElse(null);
	}

	public boolean delete(Long id) {
		return serviceRepository.findById(id)
				.map(service -> {
					service.setActive(false);
					serviceRepository.save(service);
					return true;
				})
				.orElse(false);
	}

	private List<EventType> getEventTypes(CreateServiceDto dto) {
		if (dto.getAvailableEventTypeIds() == null || dto.getAvailableEventTypeIds().isEmpty()) {
			return Collections.emptyList();
		}
		List<EventType> availableEventTypes = new ArrayList<>();
		dto.getAvailableEventTypeIds().forEach(eventTypeId ->
				eventTypeRepository.findById(eventTypeId).ifPresent(availableEventTypes::add));
		return availableEventTypes;
	}

	public Collection<ServiceDto> searchByName(int page, Integer size, String name) {
		PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);
 		return serviceRepository.searchByName(name, pageRequest)
				.stream().map(ServiceMapper::toDto)
				.toList();
	}

	public Collection<ServiceDto> filter(int page, Integer size, Float minPrice, Float maxPrice, Boolean available,
										 List<String> categories, List<Long> availableEventTypeIds) {
		PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);
		return serviceRepository.findAllFiltered(minPrice, maxPrice, available, categories, availableEventTypeIds, pageRequest)
				.stream().map(ServiceMapper::toDto)
				.toList();
	}
}