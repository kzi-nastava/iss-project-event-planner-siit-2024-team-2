package com.example.eventplanner.repositories.user;

import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.model.user.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<BaseUser, Long> {
    Optional<BaseUser> findByIdAndPassword(long id, String password);
    Optional<BaseUser> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
