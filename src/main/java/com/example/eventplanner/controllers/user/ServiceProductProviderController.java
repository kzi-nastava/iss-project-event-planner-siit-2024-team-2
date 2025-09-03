package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.user.ServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.UpdateServiceProductProviderDto;
import com.example.eventplanner.services.user.ServiceProductProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/service-product-providers")
@Validated
public class ServiceProductProviderController {
    private final ServiceProductProviderService serviceProductProviderService;

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProductProviderDto> getServiceProductProviderById(@PathVariable long id) {
        ServiceProductProviderDto dto = serviceProductProviderService.getServiceProductProviderById(id);
        return dto != null ?
                ResponseEntity.ok(dto) :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateServiceProductProviderDto> updateServiceProductProvider(@PathVariable long id, @Valid @RequestBody UpdateServiceProductProviderDto serviceProductProviderDto) {
        UpdateServiceProductProviderDto user = serviceProductProviderService.updateServiceProductProvider(id, serviceProductProviderDto);
        return user != null ?
                ResponseEntity.ok(serviceProductProviderDto) :
                ResponseEntity.notFound().build();
    }
}
