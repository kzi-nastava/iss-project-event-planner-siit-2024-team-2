package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUpgradeService {
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;

    public void upgradeToEventOrganizer(RegisterUserDto dto, BaseUser user, String encodedPassword) {
        userRepository.upgradeToEventOrganizer(
                user.getId(),
                dto.getAddress(),
                dto.getPhoneNumber(),
                dto.getFirstName(),
                dto.getLastName(),
                UserRole.EVENT_ORGANIZER.ordinal(),
                encodedPassword
        );

        em.flush();
        em.detach(user);
    }

    public void upgradeToServiceProductProvider(RegisterServiceProductProviderDto dto, BaseUser user, String encodedPassword) {
        userRepository.upgradeToServiceProductProvider(
                user.getId(),
                dto.getAddress(),
                dto.getPhoneNumber(),
                dto.getFirstName(),
                dto.getLastName(),
                UserRole.SERVICE_PRODUCT_PROVIDER.ordinal(),
                encodedPassword,
                dto.getCompanyName(),
                dto.getCompanyDescription()
        );

        em.flush();
        em.detach(user);
    }
}
