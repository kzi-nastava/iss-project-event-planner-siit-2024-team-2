
package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.user.*;
import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.dto.user.userReport.UserReportDto;
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

    @GetMapping()
    public ResponseEntity<List<RegisterUserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterUserDto> getUserByUd(@PathVariable long id) {
        RegisterUserDto registerUserDto = userService.getUserById(id);
        if (registerUserDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registerUserDto);
    }

    @PostMapping()
    public ResponseEntity<Boolean> registerUser (@RequestBody RegisterUserDto registerUserDto) {
        return userService.registerUser(registerUserDto)
                ? new ResponseEntity<>(true, HttpStatus.CREATED)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        boolean success = userService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
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
