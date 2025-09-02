package com.example.eventplanner.dto.communication.chatmessage;

import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.communication.ChatMessage;
import com.example.eventplanner.model.user.BaseUser;

public class ChatMessageMapper {
    private ChatMessageMapper() {}

    public static ChatMessageDto toDto(ChatMessage chatMessage) {
        if  (chatMessage == null) {
            return null;
        }

        return new ChatMessageDto(
                chatMessage.getId(),
                chatMessage.getText(),
                chatMessage.getSentAt(),
                chatMessage.isSeen(),
                UserMapper.toBaseUserDto(chatMessage.getFromUser())
        );
    }

    public static ChatMessage toEntity(ChatMessageNoIdDto chatMessageDto, BaseUser baseUser) {
        if  (chatMessageDto == null) {
            return null;
        }
        return new ChatMessage(
                chatMessageDto.getText(),
                chatMessageDto.isSeen(),
                baseUser
        );
    }
}
