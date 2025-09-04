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
public class ChatMessageNoIdDto {
    String text;
    boolean seen;
    Long toUserId;

    public ChatMessageNoIdDto(String text, Boolean seen, Long toUserId) {
        this.text = text;
        this.seen = seen;
        this.toUserId = toUserId;
    }
}
