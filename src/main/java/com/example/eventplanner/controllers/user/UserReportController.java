package com.example.eventplanner.controllers.user;

import com.example.eventplanner.services.user.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-reports")
@RequiredArgsConstructor()
public class UserReportController {
    private final UserReportService userReportService;
}
