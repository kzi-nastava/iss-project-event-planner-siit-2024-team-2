
package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.dto.user.userReport.UserReportDto;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    @GetMapping("/{id}/reports")
    public ResponseEntity<Collection<UserReportDto>> getUserReports(@PathVariable long id,
                                                                    @RequestParam(required = false) Boolean approved) {
        Collection<UserReportDto> result = userService.getUserReports(id, approved);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
