package com.example.eventplanner.controllers.utils;

import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.services.user.EventOrganizerService;
import com.example.eventplanner.services.user.ServiceProductProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class AuthUtil {
    private final ServiceProductProviderService serviceProductProviderService;
    private final EventOrganizerService eventOrganizerService;

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
        return eventOrganizerService.findByUsername(username);
    }
}

