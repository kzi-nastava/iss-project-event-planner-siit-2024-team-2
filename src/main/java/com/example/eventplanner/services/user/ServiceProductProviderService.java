package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.ServiceProductProviderMapper;
import com.example.eventplanner.dto.user.user.UpdateServiceProductProviderDto;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.ServiceProductProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ServiceProductProviderService {
    private final ServiceProductProviderRepository serviceProductProviderRepository;

    public RegisterServiceProductProviderDto getServiceProductProviderById(long id) {
        return serviceProductProviderRepository.findById(id)
                .map(ServiceProductProviderMapper::toDto)
                .orElse(null);
    }

    public UpdateServiceProductProviderDto updateServiceProductProvider(long id, UpdateServiceProductProviderDto serviceProductProviderDto) {
        return serviceProductProviderRepository.findById(id)
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

    public ServiceProductProvider findByUsername(String username) {
        return serviceProductProviderRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("User with username " + username + " not found"));
    }
}
