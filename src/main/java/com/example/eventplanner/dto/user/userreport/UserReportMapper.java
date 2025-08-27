package com.example.eventplanner.dto.user.userreport;

import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.UserReport;

import java.util.Date;

public class UserReportMapper {
    private UserReportMapper() {}

    public static UserReportDto toDto(UserReport userReport) {
        if (userReport == null)
            return null;

        return new UserReportDto(
                userReport.getId(),
                UserMapper.toBaseUserDto(userReport.getReporter()),
                UserMapper.toBaseUserDto(userReport.getReported()),
                userReport.getApprovedAt(),
                userReport.getReason()
        );
    }

    public static UserReport toEntity(UserReportNoIdDto dto, BaseUser reporter, BaseUser reported) {
        if (dto == null)
            return null;

        return new UserReport(
                reporter,
                reported,
                null,
                dto.getReason());
    }
}