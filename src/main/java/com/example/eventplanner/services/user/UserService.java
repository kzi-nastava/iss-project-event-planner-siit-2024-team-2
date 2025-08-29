package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.auth.ResetPasswordDto;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.user.user.*;
import com.example.eventplanner.dto.user.userreport.UserReportDto;
import com.example.eventplanner.model.user.AuthenticatedUser;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserReportService userReportService;
    private final UserRepository userRepository;
    private final UserUpgradeService userUpgradeService;

    public boolean registerUser(RegisterUserDto registerUserDto) {
        if (!validateUser(registerUserDto))
            return false;

        BaseUser existingUser = userRepository.findByEmail(registerUserDto.getEmail()).orElse(null);
        if (existingUser != null && existingUser.getUserRole() == UserRole.AUTHENTICATED) {
            String encodedPassword = passwordEncoder.encode(registerUserDto.getPassword());
            userUpgradeService.upgradeToEventOrganizer(registerUserDto, existingUser, encodedPassword);
            return true;
        }
        registerUserDto.setUserRole(UserRole.EVENT_ORGANIZER);
        registerUserDto.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userRepository.save(UserMapper.toEntity(registerUserDto));
        return true;
    }

    public boolean registerCompany(RegisterServiceProductProviderDto registerCompanyDto) {
        if (!validateUser(registerCompanyDto))
            return false;
        if (!validateCompany(registerCompanyDto))
            return false;

        BaseUser existingUser = userRepository.findByEmail(registerCompanyDto.getEmail()).orElse(null);
        if (existingUser != null && existingUser.getUserRole() == UserRole.AUTHENTICATED) {
            String encodedPassword = passwordEncoder.encode(registerCompanyDto.getPassword());
            userUpgradeService.upgradeToServiceProductProvider(registerCompanyDto, existingUser, encodedPassword);
            return true;
        }

        registerCompanyDto.setUserRole(UserRole.SERVICE_PRODUCT_PROVIDER);
        registerCompanyDto.setPassword(passwordEncoder.encode(registerCompanyDto.getPassword()));
        userRepository.save(UserMapper.toEntity(registerCompanyDto));
        return true;
    }

    public BaseUser quickRegister(String email) {
        AuthenticatedUser user = new AuthenticatedUser();
        user.setEmail(email);
        user.setUserRole(UserRole.AUTHENTICATED);
        user.setAttendingEvents(new ArrayList<>()); // Initialize manually so the attendance service can access it immediately
        userRepository.saveAndFlush(user);
        return user;
    }

    public boolean validateUser(RegisterUserDto user) {
        if (user == null) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty())  return false;
        if (user.getPassword() == null || user.getPassword().length() < 6) return false;
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) return false;
        if (user.getLastName() == null || user.getLastName().isEmpty()) return false;
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10,15}")) return false;

        if (!isRegisterEmailValidWithCurrentUser(user.getEmail())) return false;

        return true;
    }

    private boolean validateCompany(RegisterServiceProductProviderDto user) {
        if (user == null) return false;
        if (user.getCompanyName() == null || user.getFirstName().isEmpty()) return false;
        if (user.getCompanyDescription() == null || user.getCompanyDescription().isEmpty()) return false;
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


    public CompanyInfoDto getCompanyById(long id) {
        ServiceProductProvider serviceProductProvider =
                (ServiceProductProvider) userRepository.findById(id).orElse(null);
        assert serviceProductProvider != null;
        return new CompanyInfoDto(serviceProductProvider.getCompanyName(), serviceProductProvider.getCompanyDescription());
    }

    public boolean delete(long id) {
        return userRepository.findById(id)
                .map(u -> {
                    u.setActive(false);
                    userRepository.save(u);
                    return true;
                }).orElse(false);
    }

    public boolean resetPassword(ResetPasswordDto resetPasswordDto, long userId) {
        return userRepository
                .findById(userId)
                .filter(u -> u.getUserRole() != UserRole.AUTHENTICATED)
                .filter(u -> passwordEncoder.matches(resetPasswordDto.getOldPassword(), u.getPassword()))
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
                    userRepository.save(u);
                    return true;
                }).orElse(false);
    }

    public Collection<UserReportDto> getUserReports(long id, Boolean approved) {
        return userReportService.getAll()
                .stream()
                .filter(report -> report.getReported().getId() == id)
                .filter(report -> approved == null || approved == (report.getDateApproved() != null))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<BaseUser> ret = userRepository.findByEmail(email);
        if (ret.isPresent()) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(email)
                    .password(ret.get().getPassword() != null ? ret.get().getPassword() : "DUMMY_PASSWORD")
                    .roles(ret.get().getUserRole().toString())
                    .build();
        }
        throw new UsernameNotFoundException("User not found with this email: " + email);
    }
    public BaseUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + email));
    }

    public UserInfoDto updateUserInfo(UserInfoDto userInfoDto, long id) {
        BaseUser user =  userRepository.findById(id).orElse(null);
        if (user == null) return null;
        user.setFirstName(userInfoDto.getFirstName());
        user.setLastName(userInfoDto.getLastName());
        user.setAddress(userInfoDto.getAddress());
        user.setPhoneNumber(userInfoDto.getPhoneNumber());
        userRepository.save(user);
        return userInfoDto;
    }

    public CompanyInfoDto updateCompanyInfo(CompanyInfoDto companyInfoDto, long id) {
        ServiceProductProvider user = (ServiceProductProvider) userRepository.findById(id).orElse(null);
        if (user == null) return null;
        user.setCompanyName(companyInfoDto.getCompanyName());
        user.setCompanyDescription(companyInfoDto.getCompanyDescription());
        userRepository.save(user);
        return companyInfoDto;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<EventDto> getAttendingEvents(long id) {
        return userRepository.findById(id)
                .map(BaseUser::getAttendingEvents)
                .map(events -> events.stream()
                        .map(EventMapper::toDto)
                        .toList())
                .orElse(new ArrayList<>());
    }

    /**
     * Checks if the user can register with the given email. Takes into account the current user.
     * @param registerEmail The email used in registration form
     * @return True if user can register with the given email, false otherwise
     */
    public boolean isRegisterEmailValidWithCurrentUser(String registerEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        BaseUser authenticatedUser = userRepository.findByEmail(email).orElse(null);

        if (authenticatedUser != null && authenticatedUser.getUserRole() == UserRole.AUTHENTICATED) { // Upgrading
            // User is trying to register with another email
            return authenticatedUser.getEmail().equals(registerEmail);
        }
        return !userRepository.existsByEmail(registerEmail);
    }

    public RegisterUserDto uploadProfilePicture(long id, String imageName) {
        BaseUser user = userRepository.findById(id).orElse(null);
        if (user == null) return null;
        user.setImage(imageName);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    public RegisterUserDto removeProfilePicture(long id) {
        BaseUser user = userRepository.findById(id).orElse(null);
        if (user == null) return null;
        user.setImage(null);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

}

