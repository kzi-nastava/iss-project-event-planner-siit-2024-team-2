package com.example.eventplanner.services.serviceproduct;

import java.util.*;

import com.example.eventplanner.dto.serviceproduct.service.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.service.ServiceMapper;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.dto.serviceproduct.service.ServiceDto;
import com.example.eventplanner.repositories.serviceproduct.ServiceRepository;
import lombok.RequiredArgsConstructor;
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

	public Collection<ServiceDto> searchByName(int page, Integer size, String name) {
		PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);
 		return serviceRepository.searchByName(name, pageRequest)
				.stream().map(ServiceMapper::toDto)
				.toList();
	}

	public Collection<ServiceDto> filter(int page, Integer size, List<String> categories, Float minPrice, Float maxPrice, Boolean available) {
		PageRequest pageRequest = PageRequest.of(page, size != null ? size : 10);
		return serviceRepository.findAllFiltered(minPrice, maxPrice, available, categories, pageRequest)
				.stream().map(ServiceMapper::toDto)
				.toList();
	}
}
