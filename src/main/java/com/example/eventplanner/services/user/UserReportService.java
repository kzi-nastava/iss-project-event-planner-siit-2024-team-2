package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.userReport.UserReportDto;
import com.example.eventplanner.dto.user.userReport.UserReportMapper;
import com.example.eventplanner.dto.user.userReport.UserReportNoIdDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.UserReport;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.repositories.user.UserReportRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

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

    public List<UserReportDto> getAll() {
        return userReportRepository.findAll()
                .stream()
                .map(UserReportMapper::toDto)
                .toList();
    }

    public UserReportDto getById(long id) {
        return userReportRepository.findById(id)
                .map(UserReportMapper::toDto)
                .orElse(null);
    }

    public UserReportDto create(UserReportNoIdDto dto) {
        BaseUser reporter = userRepository.getReferenceById(dto.getReporterId());
        BaseUser reported = userRepository.getReferenceById(dto.getReportedId());

        UserReport userReport = UserReportMapper.toEntity(dto, reporter, reported);
        userReportRepository.save(userReport);
        return UserReportMapper.toDto(userReport);
    }

    public UserReportDto update(UserReportNoIdDto dto, long id) {
        return userReportRepository.findById(id)
                .map(ur -> {
                    ur.setDateApproved(new Date(dto.getDateApproved()));
                    ur.setReason(dto.getReason());
                    BaseUser reporter = userRepository.getReferenceById(dto.getReporterId());
                    BaseUser reported = userRepository.getReferenceById(dto.getReportedId());
                    ur.setReporter(reporter);
                    ur.setReported(reported);
                    return UserReportMapper.toDto(userReportRepository.save(ur));
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        if (!userReportRepository.existsById(id))
            return false;
        userReportRepository.deleteById(id);
        return true;
    }
}
