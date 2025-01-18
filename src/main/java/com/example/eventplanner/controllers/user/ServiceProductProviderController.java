package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.UpdateServiceProductProviderDto;
import com.example.eventplanner.services.user.ServiceProductProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/service-product-providers")
public class ServiceProductProviderController {
    private final ServiceProductProviderService serviceProductProviderService;
    @PostMapping
    public ResponseEntity<Boolean> registerServiceProductProvider (@RequestBody RegisterServiceProductProviderDto registerServiceProductProvider) {
        return serviceProductProviderService.registerServiceProductProvider(registerServiceProductProvider)
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterServiceProductProviderDto> getServiceProductProviderById(@PathVariable long id) {
        RegisterServiceProductProviderDto registerServiceProductProviderDto = serviceProductProviderService.getServiceProductProviderById(id);
        return registerServiceProductProviderDto != null ?
                ResponseEntity.ok(registerServiceProductProviderDto) :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateServiceProductProviderDto> updateServiceProductProvider(@PathVariable long id, @RequestBody UpdateServiceProductProviderDto serviceProductProviderDto) {
        UpdateServiceProductProviderDto user = serviceProductProviderService.updateServiceProductProvider(id, serviceProductProviderDto);
        return user != null ?
                ResponseEntity.ok(serviceProductProviderDto) :
                ResponseEntity.notFound().build();
    }
}
