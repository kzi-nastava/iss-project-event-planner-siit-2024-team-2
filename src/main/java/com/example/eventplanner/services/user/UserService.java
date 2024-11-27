package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.user.Admin;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.UserRole;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 0;
    public UserService() {
        //Admin
        Admin admin = new Admin();
        admin.setId(++idCounter);
        admin.setUserRole(UserRole.ADMIN);
        admin.setEmail("admin@gmail.com");
        admin.setPassword("password");
        admin.setActive(true);
        users.put(admin.getId(), admin);
    }
    public boolean registerEventOrganizer(RegisterEventOrganizerDto registerEventOrganizerDto) {
        if (!validateEventOrganizer(registerEventOrganizerDto))
            return false;
        registerEventOrganizerDto.setId(++idCounter);
        users.put(registerEventOrganizerDto.getId(), UserMapper.toEntity(registerEventOrganizerDto));
        return true;
    }

    public boolean registerServiceProductProvider(RegisterServiceProductProviderDto registerServiceProductProvider) {
        if (!validateServiceProductProvider(registerServiceProductProvider))
            return false;
        registerServiceProductProvider.setId(++idCounter);
        users.put(registerServiceProductProvider.getId(), UserMapper.toEntity(registerServiceProductProvider));
        return true;
    }

    private boolean validateEventOrganizer(RegisterEventOrganizerDto user) {
        if (user == null) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return false;
        }
        if (users.values().stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            return false;
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return false;
        }

        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return false;
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return false;
        }

        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10,15}")) {
            return false;
        }

        return true;
    }

    private boolean validateServiceProductProvider(RegisterServiceProductProviderDto user) {
        if (user == null) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return false;
        }
        if (users.values().stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            return false;
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return false;
        }

        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return false;
        }

        if (user.getCompanyName() == null || user.getCompanyName().isEmpty()) {
            return false;
        }

        if (user.getCompanyDescription() == null || user.getCompanyDescription().isEmpty()) {
            return false;
        }

        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10,15}")) {
            return false;
        }

        return true;
    }

    public List<RegisterUserDto> getAllUsers() {
        return users.values().stream().filter(Entity::isActive).map(UserMapper::toDto).toList();
    }

    public RegisterEventOrganizerDto getEventOrganizerById(long id) {
        User user = users.get(id);
        if (user == null || !user.isActive() || user.getUserRole() != UserRole.EVENT_ORGANIZER) {
            return null;
        }
        return UserMapper.toDto((EventOrganizer) user);
    }

    public RegisterServiceProductProviderDto getServiceProductProviderById(long id) {
        User user = users.get(id);
        if (user == null || !user.isActive() || user.getUserRole() != UserRole.SERVICE_PRODUCT_PROVIDER) {
            return null;
        }
        return UserMapper.toDto((ServiceProductProvider) user);
    }
}
