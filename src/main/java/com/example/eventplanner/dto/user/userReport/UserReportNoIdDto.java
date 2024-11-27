package com.example.eventplanner.dto.user.userReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportNoIdDto {
    private long reporterId;
    private long reportedId;
    private Date dateApproved;
}
