
package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.user.*;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/service-product-providers")
    public ResponseEntity<Boolean> registerServiceProductProvider (@RequestBody RegisterServiceProductProviderDto registerServiceProductProvider) {
        return userService.registerServiceProductProvider(registerServiceProductProvider)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().build();
    }
    @GetMapping("/service-product-providers/{id}")
    public ResponseEntity<RegisterServiceProductProviderDto> getServiceProductProviderById(@PathVariable long id) {
        RegisterServiceProductProviderDto registerServiceProductProviderDto = userService.getServiceProductProviderById(id);
        if (registerServiceProductProviderDto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(registerServiceProductProviderDto);
    }

    @PostMapping("/event-organizers")
    public ResponseEntity<Boolean> registerEventOrganizer (@RequestBody RegisterEventOrganizerDto registerEventOrganizerDto) {
        return userService.registerEventOrganizer(registerEventOrganizerDto)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/event-organizers/{id}")
    public ResponseEntity<RegisterEventOrganizerDto> getEventOrganizerById(@PathVariable long id) {
        RegisterEventOrganizerDto registerEventOrganizerDto = userService.getEventOrganizerById(id);
        if (registerEventOrganizerDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registerEventOrganizerDto);
    }

    @GetMapping()
    public ResponseEntity<List<RegisterUserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/event-organizers/{id}")
    public ResponseEntity<UpdateEventOrganizerDto> updateEventOrganizer(@PathVariable long id, @RequestBody UpdateEventOrganizerDto eventOrganizerDto) {
        UpdateEventOrganizerDto user = userService.updateEventOrganizer(id, eventOrganizerDto);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(eventOrganizerDto);
    }

    @PutMapping("/service-product-providers/{id}")
    public ResponseEntity<UpdateServiceProductProviderDto> updateServiceProductProvider(@PathVariable long id, @RequestBody UpdateServiceProductProviderDto serviceProductProviderDto) {
        UpdateServiceProductProviderDto user = userService.updateServiceProductProvider(id, serviceProductProviderDto);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(serviceProductProviderDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        boolean success = userService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
