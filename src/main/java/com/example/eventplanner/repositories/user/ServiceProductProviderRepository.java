package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.user.ServiceProductProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceProductProviderRepository extends JpaRepository<ServiceProductProvider, Long> {
    boolean existsByEmail(String email);
}
