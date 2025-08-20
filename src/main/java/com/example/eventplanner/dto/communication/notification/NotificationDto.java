package com.example.eventplanner.dto.communication.notification;

import com.example.eventplanner.dto.user.user.BaseUserDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private Instant sentAt;
    private boolean seen;
    private boolean dismissed;
    private BaseUserDto userDto;
}
