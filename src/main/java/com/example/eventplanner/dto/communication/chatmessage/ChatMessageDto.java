package com.example.eventplanner.dto.communication.chatmessage;

import com.example.eventplanner.dto.user.user.BaseUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    Long id;
    String text;
    Instant sentAt;
    boolean seen;
    BaseUserDto fromUser;
}
