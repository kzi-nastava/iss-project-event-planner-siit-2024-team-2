package com.example.eventplanner.services.communication;

import com.example.eventplanner.dto.communication.MessageDto;
import com.example.eventplanner.dto.communication.chatmessage.ChatMessageDto;
import com.example.eventplanner.dto.communication.chatmessage.ChatMessageMapper;
import com.example.eventplanner.dto.communication.chatmessage.ChatMessageNoIdDto;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.model.communication.ChatMessage;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.repositories.communication.ChatMessageRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final WebSocketService webSocketService;

    public ChatMessageDto getById(long id) {
        return chatMessageRepository.findById(id)
                .map(ChatMessageMapper::toDto)
                .orElse(null);
    }

    public ChatMessageDto create(ChatMessageNoIdDto dto) {
        BaseUser fromUser = userRepository.findById(dto.getToUserId()).orElseThrow();
        ChatMessage chatMessage = ChatMessageMapper.toEntity(dto, fromUser);
        return  ChatMessageMapper.toDto(chatMessageRepository.save(chatMessage));
    }

    public ChatMessageDto updateText(long id, String newText) {
        return chatMessageRepository.findById(id)
                .map(chatMessage -> {
                    chatMessage.setText(newText);
                    return ChatMessageMapper.toDto(chatMessageRepository.save(chatMessage));
                }).orElse(null);
    }

    @Transactional
    public void seen(Collection<Long> ids, long myId) {
        Long[] idsArray = ids.toArray(new Long[0]);
        chatMessageRepository.seen(idsArray,  myId);
    }

    public boolean delete(long id) {
        if (!chatMessageRepository.existsById(id))
            return false;
        chatMessageRepository.deleteById(id);
        return true;
    }

    public ChatMessageDto sendMessage(ChatMessageNoIdDto dto) {
        ChatMessageDto message = create(dto);
        MessageDto messageDto = MessageDto.builder()
                .message(dto.getText())
                .title("New message")
                .topic("chat")
                .toId(String.valueOf(dto.getToUserId()))
                .build();
        webSocketService.trySend(messageDto);
        return message;
    }
}
