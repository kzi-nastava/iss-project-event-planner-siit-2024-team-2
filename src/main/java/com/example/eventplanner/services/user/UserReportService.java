package com.example.eventplanner.services.user;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.user.userreport.UserReportDto;
import com.example.eventplanner.dto.user.userreport.UserReportMapper;
import com.example.eventplanner.dto.user.userreport.UserReportNoIdDto;
import com.example.eventplanner.exception.NotFoundException;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.UserReport;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.repositories.user.UserReportRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserReportService {
    private final UserReportRepository userReportRepository;
    private final ServiceProductRepository serviceProductRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Value("${suspension.period.days}")
    private int suspensionPeriodDays;

    public List<UserReportDto> getAll() {
        return userReportRepository.findAll()
                .stream()
                .map(UserReportMapper::toDto)
                .toList();
    }

    public Page<UserReportDto> getAllNotApproved(Pageable pageable) {
        return userReportRepository.findAllByApprovedAtIsNull(pageable)
                .map(UserReportMapper::toDto);
    }

    public UserReportDto getById(long id) {
        return userReportRepository.findById(id)
                .map(UserReportMapper::toDto)
                .orElse(null);
    }

    public UserReportDto create(UserReportNoIdDto dto) {
        BaseUser reporter = authUtil.getAuthenticatedUser();
        if (reporter == null)
            return null;
        BaseUser reported = userRepository.findByEmail(dto.getReportedEmail()).orElse(null);
        if (reported == null)
            return null;

        UserReport userReport = UserReportMapper.toEntity(dto, reporter, reported);
        return UserReportMapper.toDto(userReportRepository.save(userReport));
    }

    public boolean delete(long id) {
        if (!userReportRepository.existsById(id))
            return false;
        userReportRepository.deleteById(id);
        return true;
    }

    @Transactional
    public UserReportDto approve(long id) {
        UserReport userReport = userReportRepository.findById(id).orElse(null);
        if (userReport == null)
            throw new NotFoundException("User report not found");
        BaseUser user = userRepository.findById(userReport.getReported().getId()).orElse(null);
        if (user == null)
            throw new NotFoundException("User not found");

        userReport.setApprovedAt(Instant.now());
        userReportRepository.save(userReport);
        user.setSuspendedAt(Instant.now());
        userRepository.save(user);

        return UserReportMapper.toDto(userReport);
    }

    /** @return true if user is suspended, false if not */
    public boolean checkAndUpdateSuspension(BaseUser user) {
        if (user.getSuspendedAt() == null)
            return false;

        if (user.getSuspendedAt().plus(suspensionPeriodDays, ChronoUnit.DAYS)
                                        .isBefore(Instant.now())) {
            user.setSuspendedAt(null);
            userRepository.save(user);
            return false;
        } else
            return true;
    }
}
