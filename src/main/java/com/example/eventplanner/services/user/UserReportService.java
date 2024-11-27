package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.user.userReport.UserReportDto;
import com.example.eventplanner.dto.user.userReport.UserReportMapper;
import com.example.eventplanner.dto.user.userReport.UserReportNoIdDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.user.UserReport;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@NoArgsConstructor
public class UserReportService {
    Map<Long, UserReport> userReports = new HashMap<>();
    private long idCounter = 0;

    public List<UserReportDto> getAll() {
        return userReports.values()
                .stream()
                .filter(Entity::isActive)
                .map(UserReportMapper::toDto)
                .toList();
    }

    public UserReportDto getById(long id) {
        if (!userReports.containsKey(id) || !userReports.get(id).isActive())
            return null;
        return UserReportMapper.toDto(userReports.get(id));
    }

    public UserReportDto create(UserReportNoIdDto dto) {
        UserReport userReport = UserReportMapper.toEntity(dto);
        userReport.setId(idCounter++);
        userReport.setActive(true);

        // link reporter
        User testReporter = new User();
        testReporter.setId(dto.getReporterId());
        userReport.setReporter(testReporter);
        // link reported
        User testReported = new User();
        testReported.setId(dto.getReportedId());
        userReport.setReported(testReported);

        userReports.put(userReport.getId(), userReport);
        return UserReportMapper.toDto(userReport);
    }

    public UserReportDto update(UserReportNoIdDto dto, long id) {
        if (this.getById(id) == null)
            return null;
        UserReport userReport = UserReportMapper.toEntity(dto);

        // link reporter
        User testReporter = new User();
        testReporter.setId(dto.getReporterId());
        userReport.setReporter(testReporter);
        // link reported
        User testReported = new User();
        testReported.setId(dto.getReportedId());
        userReport.setReported(testReported);

        userReports.put(id, UserReportMapper.toEntity(dto));
        return UserReportMapper.toDto(userReport);
    }

    public boolean delete(long id) {
        if (this.getById(id) == null)
            return false;
        userReports.get(id).setActive(false);
        return true;
    }
}
