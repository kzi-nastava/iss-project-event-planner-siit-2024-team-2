package com.example.eventplanner.controllers.utils;

import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.services.user.EventOrganizerService;
import com.example.eventplanner.services.user.ServiceProductProviderService;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthUtil {
    private final ServiceProductProviderService serviceProductProviderService;
    private final EventOrganizerService eventOrganizerService;
    private final UserService userService;

    public ServiceProductProvider getAuthenticatedServiceProductProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return serviceProductProviderService.findByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

    public EventOrganizer getAuthenticatedEventOrganizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return eventOrganizerService.findByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            BaseUser user = userService.findByUsername(username);
            return user != null ? user.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
}

