
package com.example.eventplanner.controllers.user;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.user.user.CompanyInfoDto;
import com.example.eventplanner.dto.user.user.RegisterUserDto;
import com.example.eventplanner.dto.user.user.UserInfoDto;
import com.example.eventplanner.dto.user.userreport.UserReportDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthUtil authUtil;

    @GetMapping()
    public ResponseEntity<List<RegisterUserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterUserDto> getUserById(@PathVariable long id) {
        RegisterUserDto registerUserDto = userService.getUserById(id);
        return registerUserDto != null ?
                ResponseEntity.ok(registerUserDto) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<CompanyInfoDto> getCompanyById(@PathVariable long id) {
        CompanyInfoDto companyDto = userService.getCompanyById(id);
        return companyDto != null ?
                ResponseEntity.ok(companyDto) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Boolean> registerUser (@RequestBody RegisterUserDto registerUserDto) {
        return userService.registerUser(registerUserDto)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfoDto> updateUserInfo(@PathVariable long id, @RequestBody UserInfoDto userInfoDto) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (user.getId() != id && user.getUserRole() != UserRole.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserInfoDto userInfoDto1 = userService.updateUserInfo(userInfoDto, id);
        return userInfoDto1 != null
                ? ResponseEntity.ok(userInfoDto1)
                : ResponseEntity.badRequest().build();
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<CompanyInfoDto> updateCompanyInfo(@PathVariable long id, @RequestBody CompanyInfoDto companyInfoDto) {
        CompanyInfoDto companyInfoDto1 = userService.updateCompanyInfo(companyInfoDto, id);
        return companyInfoDto1 != null
                ? ResponseEntity.ok(companyInfoDto1)
                : ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (user.getId() != id && user.getUserRole() != UserRole.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        boolean success = userService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/attended-events")
    public ResponseEntity<Collection<EventDto>> getAttendingEvents(@PathVariable long id) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (user.getId() != id && user.getUserRole() != UserRole.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Collection<EventDto> result = userService.getAttendingEvents(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
