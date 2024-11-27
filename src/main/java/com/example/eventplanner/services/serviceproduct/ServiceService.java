package com.example.eventplanner.services.serviceproduct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.example.eventplanner.dto.serviceproduct.CreateServiceDto;
import org.springframework.stereotype.Service;

import com.example.eventplanner.dto.serviceproduct.ServiceDto;


@Service
public class ServiceService {
	private static AtomicLong counter = new AtomicLong();
	private HashMap<Long, ServiceDto> services = new HashMap<Long, ServiceDto>();

	public Collection<ServiceDto> getAll() {
		return services.values().stream().filter(ServiceDto::isActive).toList();
	}
	
	public ServiceDto getById(Long id) {
		ServiceDto serviceDto = services.get(id);
		if (serviceDto != null && serviceDto.isActive())
			return serviceDto;
		return null;
	}
	
	public ServiceDto create(CreateServiceDto service) {
		Long id = counter.incrementAndGet();
		ServiceDto ServiceDto = new ServiceDto(id, service);
		services.put(id, ServiceDto);
		return ServiceDto;
	}

	public ServiceDto update(Long id, CreateServiceDto serviceDto) {
		if (this.getById(id) == null) {
			return null;
		}
		ServiceDto updatedServiceDto = new ServiceDto(id, serviceDto);
		services.put(id, updatedServiceDto);
		return updatedServiceDto;
	}

	public boolean delete(Long id) {
		if (services.containsKey(id)) {
			services.get(id).setActive(false);
			return true;
		}
		return false;
	}
}
