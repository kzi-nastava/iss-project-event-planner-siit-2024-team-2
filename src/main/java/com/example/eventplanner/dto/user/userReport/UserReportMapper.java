package com.example.eventplanner.dto.user.userReport;

import com.example.eventplanner.model.user.UserReport;

public class UserReportMapper {
    public static UserReportDto toDto(UserReport userReport) {
        if (userReport == null)
            return null;
        return new UserReportDto(
                userReport.getId(),
                userReport.getReporter().getId(),
                userReport.getReported().getId(),
                userReport.getDateApproved()
        );
    }

    public static UserReportNoIdDto toDtoNoId(UserReport userReport) {
        if (userReport == null)
            return null;
        return new UserReportNoIdDto(
                userReport.getReporter().getId(),
                userReport.getReported().getId(),
                userReport.getDateApproved()
        );
    }

    public static UserReport toEntity(UserReportDto dto) {
        if (dto == null)
            return null;
        UserReport userReport = new UserReport(
                null,
                null,
                dto.getDateApproved());
        userReport.setId(dto.getId());
        userReport.setActive(true);
        return userReport;
    }

    public static UserReport toEntity(UserReportNoIdDto dto) {
        if (dto == null)
            return null;
        UserReport userReport = new UserReport(
                null,
                null,
                dto.getDateApproved());
        userReport.setActive(true);
        return userReport;
    }
}