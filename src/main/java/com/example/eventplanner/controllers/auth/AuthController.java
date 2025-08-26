package com.example.eventplanner.controllers.auth;

import com.example.eventplanner.config.jwt.JwtTokenUtil;
import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.auth.LoginDto;
import com.example.eventplanner.dto.auth.LoginResponseDto;
import com.example.eventplanner.dto.auth.QuickLoginDto;
import com.example.eventplanner.dto.auth.ResetPasswordDto;
import com.example.eventplanner.dto.event.invitation.InvitationDto;
import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.services.event.InvitationService;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;
    private final InvitationService invitationService;
    private final AuthUtil authUtil;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        BaseUser authenticatedUser = userService.getUserByEmail(request.getEmail());

        String token = jwtTokenUtil.generateToken(authenticatedUser.getEmail());

        return new LoginResponseDto(authenticatedUser.getId(),
                authenticatedUser.getEmail(),
                token,
                authenticatedUser.getUserRole());
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> registerUser (@RequestBody RegisterUserDto registerUserDto) {
        return userService.registerUser(registerUserDto)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/signup/company")
    public ResponseEntity<Boolean> registerCompany(@RequestBody RegisterServiceProductProviderDto registerCompanyDto) {
        return userService.registerCompany(registerCompanyDto)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().build();
    }
    @PostMapping("/reset-password/{id}")
    public ResponseEntity<Void> resetPassword(@PathVariable long id, @RequestBody ResetPasswordDto resetPasswordDto) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (user.getId() != id && user.getUserRole() != UserRole.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        boolean success = userService.resetPassword(resetPasswordDto, id);
        return success
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/quick-login")
    public ResponseEntity<LoginResponseDto> quickLogin(@RequestBody QuickLoginDto quickLoginDto) {
        InvitationDto invitation = invitationService.getByToken(quickLoginDto.getInvitationToken());
        if (invitation == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        BaseUser user = userService.getUserByEmail(invitation.getEmail());
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (!invitation.isQuickRegistration() || !invitation.isAccepted() || user.getUserRole() != UserRole.AUTHENTICATED)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        String jwt = jwtTokenUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponseDto(
                user.getId(),
                user.getEmail(),
                jwt,
                user.getUserRole()
        ));
    }
}
