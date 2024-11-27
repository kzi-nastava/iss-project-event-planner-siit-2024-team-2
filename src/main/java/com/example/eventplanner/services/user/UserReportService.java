package com.example.eventplanner.services.user;

import com.example.eventplanner.model.user.UserReport;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class UserReportService {
    List<UserReport> userReports = new ArrayList<>();
    public UserReportService() {}
}
