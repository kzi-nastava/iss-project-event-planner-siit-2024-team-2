package com.example.eventplanner.services.serviceproduct;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.example.eventplanner.dto.serviceproduct.service.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceMapper;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import lombok.RequiredArgsConstructor;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
	private final HashMap<Long, Service> services = new HashMap<>();
	private final ServiceRepository serviceRepository;

	public Collection<ServiceDto> getAll() {
		return serviceRepository.findAllByIsActiveTrue()
				.stream()
				.map(ServiceMapper::toDto)
				.toList();
	}
	
	public ServiceDto getById(Long id) {
		return serviceRepository.findByIdAndIsActiveTrue(id)
				.map(ServiceMapper::toDto)
				.orElse(null);
	}
	
	public ServiceDto create(CreateServiceDto createServiceDto) {
		Service service = serviceRepository.save(ServiceMapper.toEntity(createServiceDto));
		return ServiceMapper.toDto(service);
	}

	public ServiceDto update(Long id, CreateServiceDto createServiceDto) {
		if (this.getById(id) == null) {
			return null;
		}
		ServiceDto updatedServiceDto = new ServiceDto(id, createServiceDto);
		serviceRepository.save(ServiceMapper.toEntity(updatedServiceDto));
		return updatedServiceDto;
	}

	public boolean delete(Long id) {
		return serviceRepository.findByIdAndIsActiveTrue(id)
				.map(service -> {
					service.setActive(false);
					serviceRepository.save(service);
					return true;
				})
				.orElse(false);
	}

	public Collection<ServiceDto> searchByName(String name) {
		return serviceRepository.searchByName(name)
				.stream().map(ServiceMapper::toDto)
				.toList();
	}

	public Collection<ServiceDto> filter(List<ServiceProductCategory> categories, List<String> eventTypes, Float minPrice, Float maxPrice, Boolean available) {
		List<String> categoryNames = new ArrayList<>();
		if (categories != null) {
			for (ServiceProductCategory category : categories)
				categoryNames.add(category.getName());
		}

		return services.values().stream()
				.filter(service -> categoryNames.isEmpty() || categoryNames.contains(service.getCategory().getName()))
				.filter(service -> eventTypes == null || service.getAvailableEventTypes().stream().map(EventType::getName).anyMatch(eventTypes::contains))
				.filter(service -> available == null || available == service.isAvailable())
				.filter(service -> (minPrice == null || minPrice <= service.getPrice()) && (maxPrice == null || maxPrice >= service.getPrice()))
				.map(ServiceMapper::toDto).toList();
	}
}
