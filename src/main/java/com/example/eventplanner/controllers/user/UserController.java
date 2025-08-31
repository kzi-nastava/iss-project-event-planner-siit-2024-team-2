
package com.example.eventplanner.controllers.user;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.user.user.*;
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
        authUtil.checkUserAccess(id);
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
        authUtil.checkUserAccess(id);
        boolean success = userService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/attended-events")
    public ResponseEntity<Collection<EventDto>> getAttendingEvents(@PathVariable long id) {
        authUtil.checkUserAccess(id);
        Collection<EventDto> result = userService.getAttendingEvents(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/upload-picture")
    public ResponseEntity<BaseUserDto> uploadProfilePicture(
            @PathVariable long id,
            @RequestBody ImageNameDto dto) {
        authUtil.checkUserAccess(id);
        BaseUserDto updatedUser = userService.uploadProfilePicture(id, dto.getImageName());
        return updatedUser != null
                ? ResponseEntity.ok(updatedUser)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/remove-picture")
    public ResponseEntity<BaseUserDto> removeProfilePicture(@PathVariable long id) {
        authUtil.checkUserAccess(id);
        BaseUserDto updatedUser = userService.removeProfilePicture(id);
        return updatedUser != null
                ? ResponseEntity.ok(updatedUser)
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/{email}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable("email") String email) {
        userService.suspendUser(email);
        return ResponseEntity.noContent().build();
    }
}
