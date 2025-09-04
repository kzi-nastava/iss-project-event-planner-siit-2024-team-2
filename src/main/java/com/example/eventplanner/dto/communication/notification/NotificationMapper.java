package com.example.eventplanner.dto.communication.notification;

import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.communication.Notification;
import com.example.eventplanner.model.user.BaseUser;

public class NotificationMapper {
    private NotificationMapper() {}

    public static NotificationDto toDto(Notification notification) {
        if (notification == null)
            return null;

        return new NotificationDto(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getSentAt(),
                notification.isSeen(),
                notification.isDismissed(),
                UserMapper.toBaseUserDto(notification.getUser())
        );
    }

    public static Notification toEntity(NotificationNoIdDto dto, BaseUser user) {
        if (dto == null)
            return null;

        return new Notification(
                dto.getTitle(),
                dto.getMessage(),
                dto.isSeen(),
                dto.isDismissed(),
                user
        );
    }
}
