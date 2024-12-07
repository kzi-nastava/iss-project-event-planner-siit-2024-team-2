package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.user.*;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.ServiceProductProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceProductProviderService {
    private final ServiceProductProviderRepository serviceProductProviderRepository;

    public boolean registerServiceProductProvider(RegisterServiceProductProviderDto registerServiceProductProvider) {
        if (!validateServiceProductProvider(registerServiceProductProvider))
            return false;
        serviceProductProviderRepository.save(ServiceProductProviderMapper.toEntity(registerServiceProductProvider));
        return true;
    }

    private boolean validateServiceProductProvider(RegisterServiceProductProviderDto user) {
        if (user == null) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty()) return false;
        if (serviceProductProviderRepository.existsByEmailAndIsActiveTrue(user.getEmail())) return false;
        if (user.getPassword() == null || user.getPassword().length() < 6) return false;
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) return false;
        if (user.getCompanyName() == null || user.getCompanyName().isEmpty()) return false;
        if (user.getCompanyDescription() == null || user.getCompanyDescription().isEmpty()) return false;
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10,15}")) return false;
        return true;
    }

    public RegisterServiceProductProviderDto getServiceProductProviderById(long id) {
        return serviceProductProviderRepository.findByIdAndIsActiveTrue(id)
                .map(ServiceProductProviderMapper::toDto)
                .orElse(null);
    }


    public UpdateServiceProductProviderDto updateServiceProductProvider(long id, UpdateServiceProductProviderDto serviceProductProviderDto) {
        return serviceProductProviderRepository.findByIdAndIsActiveTrue(id)
                .map(existing -> {
                    ServiceProductProvider serviceProductProvider = ServiceProductProviderMapper.toUpdateEntity(serviceProductProviderDto);
                    serviceProductProvider.setActive(true);
                    serviceProductProvider.setId(id);
                    serviceProductProvider.setEmail(existing.getEmail());
                    serviceProductProvider.setPassword(existing.getPassword());
                    serviceProductProvider.setUserRole(UserRole.SERVICE_PRODUCT_PROVIDER);
                    serviceProductProvider.setFirstName(serviceProductProviderDto.getFirstName());
                    serviceProductProvider.setLastName(serviceProductProviderDto.getLastName());
                    serviceProductProvider.setAddress(serviceProductProviderDto.getAddress());
                    serviceProductProvider.setPhoneNumber(serviceProductProviderDto.getPhoneNumber());
                    serviceProductProvider.setCompanyName(existing.getCompanyName());
                    serviceProductProvider.setCompanyDescription(serviceProductProviderDto.getCompanyDescription());
                    return ServiceProductProviderMapper.toUpdateDto(serviceProductProviderRepository.save(serviceProductProvider));
                }).orElse(null);
    }
}
