package com.example.eventplanner.dto.user.userreport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportNoIdDto {
    private long reportedId;
    private String reason;
}
