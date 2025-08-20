package com.example.eventplanner.dto.communication.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationNoIdDto {
    private String title;
    private String message;
    private boolean seen;
    private boolean dismissed;
    private long userId;

    public NotificationNoIdDto(String title, String message, long userId) {
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.seen = false;
        this.dismissed = false;
    }
}
