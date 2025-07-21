package com.example.eventplanner.dto.user.userReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportNoIdDto {
    private long reporterId;
    private long reportedId;
    private Long dateApproved;
    private String reason;
}
