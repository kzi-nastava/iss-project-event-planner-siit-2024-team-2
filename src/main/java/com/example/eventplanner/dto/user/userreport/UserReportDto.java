package com.example.eventplanner.dto.user.userreport;

import com.example.eventplanner.dto.user.user.BaseUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportDto {
    private long id;
    private BaseUserDto reporter;
    private BaseUserDto reported;
    private Instant approvedAt = null;
    private String reason;
    private Instant createdAt;
}
