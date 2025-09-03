package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.userreport.UserReportDto;
import com.example.eventplanner.dto.user.userreport.UserReportNoIdDto;
import com.example.eventplanner.services.user.UserReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/user-reports")
@RequiredArgsConstructor()
@Validated
public class UserReportController {
    private final UserReportService userReportService;

    @GetMapping
    public ResponseEntity<Collection<UserReportDto>> getAllUserReports() {
        Collection<UserReportDto> result = userReportService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/not-approved")
    public ResponseEntity<Page<UserReportDto>> getAllNotApproved(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        Pageable pageable = PageRequest.of(page, size != null ? size : 10).withSort(Sort.by(Sort.Direction.DESC, "id"));
        Page<UserReportDto> result = userReportService.getAllNotApproved(pageable);
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
    public ResponseEntity<UserReportDto> createUserReport(@Valid @RequestBody UserReportNoIdDto dto) {
        UserReportDto result = userReportService.create(dto);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserReportDto> deleteUserReport(@PathVariable("id") Long id) {
        boolean success = userReportService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<UserReportDto> approveUserReport(@PathVariable("id") Long id) {
        UserReportDto result = userReportService.approve(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
