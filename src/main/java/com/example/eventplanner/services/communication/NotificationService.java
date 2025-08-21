package com.example.eventplanner.services.communication;

import com.example.eventplanner.dto.communication.MessageDto;
import com.example.eventplanner.dto.communication.notification.NotificationDto;
import com.example.eventplanner.dto.communication.notification.NotificationMapper;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.model.communication.Notification;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.repositories.communication.NotificationRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WebSocketService webSocketService;

    public Page<NotificationDto> getAll(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(NotificationMapper::toDto);
    }

    public Page<NotificationDto> getAllForUser(Long userId, Instant sentAt, Pageable pageable) {
        if (sentAt != null)
            return notificationRepository.findAllByUserIdAndDismissedFalseAndSentAtBefore(userId, sentAt, pageable)
                    .map(NotificationMapper::toDto);
        else
            return notificationRepository.findAllByUserIdAndDismissedFalse(userId, pageable)
                    .map(NotificationMapper::toDto);
    }

    public NotificationDto get(long id) {
        return notificationRepository.findById(id)
                .map(NotificationMapper::toDto)
                .orElse(null);
    }

    public NotificationDto create(NotificationNoIdDto dto) {
        BaseUser user = userRepository.findById(dto.getUserId()).orElseThrow();
        Notification notification = NotificationMapper.toEntity(dto, user);
        return NotificationMapper.toDto(notificationRepository.save(notification));
    }

    public NotificationDto update(NotificationNoIdDto dto, long id) {
        BaseUser user = userRepository.findById(dto.getUserId()).orElseThrow();
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setTitle(dto.getTitle());
                    notification.setMessage(dto.getMessage());
                    notification.setUser(user);
                    notification.setSeen(dto.isSeen());
                    notification.setDismissed(dto.isDismissed());
                    return NotificationMapper.toDto(notificationRepository.save(notification));
        }).orElse(null);
    }

    @Transactional
    public void dismiss(Collection<Long> ids, Long userId) {
        Long[] idsArray = ids.toArray(new Long[0]);
        if (!notificationRepository.hasAccess(idsArray, userId))
            throw new AccessDeniedException("Access denied");
        notificationRepository.dismiss(idsArray, userId);
    }

    @Transactional
    public void seen(Collection<Long> ids, Long userId) {
        Long[] idsArray = ids.toArray(new Long[0]);
        if (!notificationRepository.hasAccess(idsArray, userId))
            throw new AccessDeniedException("Access denied");
        notificationRepository.seen(idsArray, userId);
    }

    public boolean delete(long id) {
        if (!notificationRepository.existsById(id))
            return false;
        notificationRepository.deleteById(id);
        return true;
    }

    @Async
    public void sendNotification(NotificationNoIdDto dto) {
        create(dto);
        MessageDto messageDto = MessageDto.builder()
                .message(dto.getMessage())
                .title(dto.getTitle())
                .topic("notifications")
                .toId(String.valueOf(dto.getUserId()))
                .build();
        webSocketService.trySend(messageDto);
    }
}
