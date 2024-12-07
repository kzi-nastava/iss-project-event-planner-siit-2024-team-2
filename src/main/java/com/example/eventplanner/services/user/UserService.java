package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.auth.ResetPasswordDto;
import com.example.eventplanner.dto.user.user.*;
import com.example.eventplanner.dto.auth.LoginDto;
import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.userReport.UserReportDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.Admin;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import com.example.eventplanner.repositories.user.ServiceProductProviderRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserReportService userReportService;
    private final UserRepository userRepository;

    public boolean registerUser(RegisterUserDto registerUserDto) {
        if (!validateUser(registerUserDto))
            return false;
        userRepository.save(UserMapper.toEntity(registerUserDto));
        return true;
    }

    private boolean validateUser(RegisterUserDto user) {
        if (user == null) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty())  return false;
        if (userRepository.existsByEmail(user.getEmail())) return false;
        if (user.getPassword() == null || user.getPassword().length() < 6) return false;
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) return false;
        if (user.getLastName() == null || user.getLastName().isEmpty()) return false;
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10,15}")) return false;
        return true;
    }

    public List<RegisterUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public RegisterUserDto getUserById(long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElse(null);
    }

    public boolean delete(long id) {
        return userRepository.findById(id)
                .map(u -> {
                    u.setActive(false);
                    userRepository.save(u);
                    return true;
                }).orElse(false);
    }

    public RegisterUserDto login(LoginDto loginDto) {
        return UserMapper.toDto(userRepository
                .findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())
                .orElse(null));
    }

    public boolean resetPassword(ResetPasswordDto resetPasswordDto) {
        return userRepository
                .findByIdAndPassword(resetPasswordDto.getUserId(), resetPasswordDto.getOldPassword())
                .map(u -> {
                    u.setPassword(resetPasswordDto.getNewPassword());
                    userRepository.save(u);
                    return true;
                }).orElse(false);
    }

    public Collection<UserReportDto> getUserReports(long id, Boolean approved) {
        return userReportService.getAll()
                .stream()
                .filter(report -> report.getReportedId() == id)
                .filter(report -> approved == null || approved == (report.getDateApproved() != null))
                .toList();
    }
}

