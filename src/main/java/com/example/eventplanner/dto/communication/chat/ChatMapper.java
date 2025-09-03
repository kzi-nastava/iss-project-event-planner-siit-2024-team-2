package com.example.eventplanner.dto.communication.chat;

import com.example.eventplanner.dto.communication.chatmessage.ChatMessageMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.communication.Chat;
import com.example.eventplanner.model.communication.ChatMessage;
import com.example.eventplanner.model.user.BaseUser;

import java.util.List;

public class ChatMapper {
    private ChatMapper() {}

    public static ChatDto toDto(Chat chat) {
        if (chat == null) return null;
        return new ChatDto(
                chat.getId(),
                UserMapper.toBaseUserDto(chat.getUser1()),
                UserMapper.toBaseUserDto(chat.getUser2()),
                chat.getMessages().stream().map(ChatMessageMapper::toDto).toList(),
                chat.getStatus(),
                chat.getSentAt()

        );
    }

    public static Chat toEntity(ChatNoIdDto dto, BaseUser user1, BaseUser user2, List<ChatMessage> messages) {
        if (dto == null) return null;
        return new Chat(user1, user2, messages, dto.getStatus(),  dto.getSentAt());
    }
}
