package com.example.eventplanner.controllers.auth;

import com.example.eventplanner.dto.auth.LoginDto;
import com.example.eventplanner.dto.auth.ResetPasswordDto;
import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<RegisterUserDto> login(@RequestBody LoginDto loginDto) {
        RegisterUserDto user = userService.login(loginDto);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        boolean success = userService.resetPassword(resetPasswordDto);
        return success
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
