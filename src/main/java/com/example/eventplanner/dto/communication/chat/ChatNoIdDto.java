package com.example.eventplanner.dto.communication.chat;

import com.example.eventplanner.model.utils.ChatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatNoIdDto {
    Long toId;
    List<Long> messageIds;
    ChatStatus status;
    Instant sentAt;
}
