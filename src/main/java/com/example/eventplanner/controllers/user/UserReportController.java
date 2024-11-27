package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.userReport.UserReportDto;
import com.example.eventplanner.dto.user.userReport.UserReportNoIdDto;
import com.example.eventplanner.services.user.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/user-reports")
@RequiredArgsConstructor()
public class UserReportController {
    private final UserReportService userReportService;

    @GetMapping
    public ResponseEntity<Collection<UserReportDto>> getUserReports() {
        Collection<UserReportDto> result = userReportService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserReportDto> getUserReportById(@PathVariable("id") Long id) {
        UserReportDto result = userReportService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<UserReportDto> createUserReport(@RequestBody UserReportNoIdDto dto) {
        UserReportDto result = userReportService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserReportDto> updateUserReport(@PathVariable("id") Long id, @RequestBody UserReportNoIdDto dto) {
        UserReportDto result = userReportService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserReportDto> deleteUserReport(@PathVariable("id") Long id) {
        boolean success = userReportService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
