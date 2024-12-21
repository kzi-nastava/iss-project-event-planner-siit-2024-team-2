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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
	private final ServiceRepository serviceRepository;

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
	
	public ServiceDto create(CreateServiceDto createServiceDto) {
		Service service = serviceRepository.save(ServiceMapper.toEntity(createServiceDto));
		return ServiceMapper.toDto(service);
	}

	public ServiceDto update(Long id, CreateServiceDto createServiceDto) {
		if (this.getById(id) == null) {
			return null;
		}
		ServiceDto updatedServiceDto = new ServiceDto(id, createServiceDto);
		serviceRepository.save(ServiceMapper.toEntity(updatedServiceDto, 0));
		return updatedServiceDto;
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

	public Page<ServiceDto> searchByName(int page, Integer size, String name) {
		PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);
 		return serviceRepository.searchByName(name, pageRequest)
				.map(ServiceMapper::toDto);
	}

	public Page<ServiceDto> filter(int page, Integer size, List<String> categories, Float minPrice, Float maxPrice, boolean available) {
		PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);
		return serviceRepository.findAllFiltered(minPrice, maxPrice, available, pageRequest)
				.map(ServiceMapper::toDto);
		//		List<String> categoryNames = new ArrayList<>();
//		if (categories != null) {
//			for (ServiceProductCategory category : categories)
//				categoryNames.add(category.getName());
//		}
//
//		return services.values().stream()
//				.filter(service -> categoryNames.isEmpty() || categoryNames.contains(service.getCategory().getName()))
//				.filter(service -> eventTypes == null || service.getAvailableEventTypes().stream().map(EventType::getName).anyMatch(eventTypes::contains))
//				.filter(service -> available == null || available == service.isAvailable())
//				.filter(service -> (minPrice == null || minPrice <= service.getPrice()) && (maxPrice == null || maxPrice >= service.getPrice()))
//				.map(ServiceMapper::toDto).toList();
	}
}
