package com.example.eventplanner.dto.communication.chat;

import com.example.eventplanner.dto.communication.chatmessage.ChatMessageDto;
import com.example.eventplanner.dto.user.user.BaseUserDto;
import com.example.eventplanner.model.utils.ChatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private Long id;
    BaseUserDto user1;
    BaseUserDto user2;
    List<ChatMessageDto> messages;
    ChatStatus status;
}
