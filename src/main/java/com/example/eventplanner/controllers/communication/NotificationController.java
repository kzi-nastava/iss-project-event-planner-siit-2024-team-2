package com.example.eventplanner.controllers.communication;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.communication.notification.NotificationDto;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.services.communication.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;

@RestController()
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthUtil authUtil;

    @GetMapping()
    public ResponseEntity<Page<NotificationDto>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sentAt"));
        Page<NotificationDto> result = notificationService.getAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotification(@PathVariable Long id) {
        NotificationDto result = notificationService.get(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationNoIdDto dto) {
        NotificationDto result = notificationService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto> updateNotification(@PathVariable Long id, @RequestBody NotificationNoIdDto dto) {
        NotificationDto result = notificationService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/dismiss")
    public ResponseEntity<?> dismissNotifications(@RequestBody Collection<Long> ids) {
        long userId = authUtil.getAuthenticatedUserId();
        try {
            notificationService.dismiss(ids, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/seen")
    public ResponseEntity<?> seenNotifications(@RequestBody Collection<Long> ids) {
        Long userId = authUtil.getAuthenticatedUserId();
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            notificationService.seen(ids, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<NotificationDto> deleteEvent(@PathVariable("id") Long id) {
        boolean success = notificationService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("mine")
    public ResponseEntity<Page<NotificationDto>> getUserNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Instant sentAt) {
        Long userId = authUtil.getAuthenticatedUserId();
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sentAt"));
        Page<NotificationDto> result = notificationService.getAllForUser(userId, sentAt, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
