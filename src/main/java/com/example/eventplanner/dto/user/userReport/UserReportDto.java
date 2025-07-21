package com.example.eventplanner.dto.user.userReport;

import com.example.eventplanner.dto.user.user.BaseUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportDto {
    private long id;
    private BaseUserDto reporter;
    private BaseUserDto reported;
    private Long dateApproved;
    private String reason;
}
