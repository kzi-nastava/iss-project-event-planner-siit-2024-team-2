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


@org.springframework.stereotype.Service
public class ServiceService {
	private final static AtomicLong counter = new AtomicLong();
	private final HashMap<Long, Service> services = new HashMap<>();

	public Collection<ServiceDto> getAll() {
		return services.values().stream().filter(Entity::isActive).map(ServiceMapper::toDto).toList();
	}
	
	public ServiceDto getById(Long id) {
		Service service = services.get(id);
		if (service != null && service.isActive())
			return ServiceMapper.toDto(service);
		return null;
	}
	
	public ServiceDto create(CreateServiceDto createServiceDto) {
		Long id = counter.incrementAndGet();
		ServiceDto ServiceDto = new ServiceDto(id, createServiceDto);
		services.put(id, ServiceMapper.toEntity(ServiceDto));
		return ServiceDto;
	}

	public ServiceDto update(Long id, CreateServiceDto createServiceDto) {
		if (this.getById(id) == null) {
			return null;
		}
		ServiceDto updatedServiceDto = new ServiceDto(id, createServiceDto);
		services.put(id, ServiceMapper.toEntity(updatedServiceDto));
		return updatedServiceDto;
	}

	public boolean delete(Long id) {
		if (this.getById(id) == null) {
			return false;
		}
		services.get(id).setActive(false);
		return true;
	}

	public Collection<ServiceDto> searchByName(String name) {
		return this.getAll().stream()
							.filter(service -> service.getName() != null &&
									service.getName().toLowerCase().contains(name.toLowerCase()))
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
