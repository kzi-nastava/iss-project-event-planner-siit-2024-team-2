package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryNoIdDto;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ServiceProductCategoryService {
    private final ServiceProductCategoryRepository serviceProductCategoryRepository;

    public Collection<ServiceProductCategoryDto> getAll() {
        return serviceProductCategoryRepository.findAll()
                .stream()
                .map(ServiceProductCategoryMapper::toDto)
                .toList();
    }

    public ServiceProductCategoryDto getById(long id) {
        return serviceProductCategoryRepository.findById(id)
                .map(ServiceProductCategoryMapper::toDto)
                .orElse(null);
    }

    public ServiceProductCategoryDto create(ServiceProductCategoryNoIdDto dto) {
        ServiceProductCategory entity = serviceProductCategoryRepository.save(ServiceProductCategoryMapper.toEntity(dto));
        return ServiceProductCategoryMapper.toDto(entity);
    }

    public ServiceProductCategoryDto update(long id, ServiceProductCategoryNoIdDto dto) {
        if (this.getById(id) == null) {
            return null;
        }
        ServiceProductCategoryDto updatedDto = new ServiceProductCategoryDto();
        updatedDto.setId(id);
        updatedDto.setName(dto.getName());
        serviceProductCategoryRepository.save(ServiceProductCategoryMapper.toEntity(updatedDto));
        return updatedDto;
    }

    public boolean delete(long id) {
        return serviceProductCategoryRepository.findById(id)
                .map(category -> {
                    category.setActive(false);
                    serviceProductCategoryRepository.save(category);
                    return true;
                })
                .orElse(false);
    }

}
