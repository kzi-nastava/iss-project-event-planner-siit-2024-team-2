package com.example.eventplanner.services.communication;

import com.example.eventplanner.dto.communication.chat.ChatDto;
import com.example.eventplanner.dto.communication.chat.ChatMapper;
import com.example.eventplanner.dto.communication.chat.ChatNoIdDto;
import com.example.eventplanner.model.communication.Chat;
import com.example.eventplanner.model.communication.ChatMessage;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ChatStatus;
import com.example.eventplanner.repositories.communication.ChatRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import com.example.eventplanner.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ChatDto getById(long id) {
        return chatRepository.findById(id)
                .map(ChatMapper::toDto)
                .orElse(null);
    }

    public ChatDto create(long myId, ChatNoIdDto dto) {
        OrderedUsers users = getOrderedUsers(myId, dto.getToId());
        List<ChatMessage> messages = new ArrayList<>();
        dto.setStatus(ChatStatus.ALL_SEEN);
        dto.setSentAt(Instant.now());
        Chat chat = ChatMapper.toEntity(dto, users.user1(), users.user2(), messages);
        return ChatMapper.toDto(chatRepository.save(chat));
    }

    @NotNull
    private OrderedUsers getOrderedUsers(long user1Id, long user2Id) {
        BaseUser user1, user2;
        if (user2Id < user1Id) {
            user1 = userRepository.findById(user2Id).orElseThrow();
            user2 = userRepository.findById(user1Id).orElseThrow();
        }
        else {
            user1 = userRepository.findById(user1Id).orElseThrow();
            user2 = userRepository.findById(user2Id).orElseThrow();
        }
        return new OrderedUsers(user1, user2);
    }

    private record OrderedUsers(BaseUser user1, BaseUser user2) {}

    public boolean delete(long id) {
        if (!chatRepository.existsById(id))
            return false;
        chatRepository.deleteById(id);
        return true;
    }

    public Optional<ChatDto> getByUser1AndUser2(long user1Id, long user2Id) {
        OrderedUsers users = getOrderedUsers(user1Id, user2Id);
        return chatRepository.findByUser1IdAndUser2Id(users.user1.getId(), users.user2.getId()).map(ChatMapper::toDto);
    }

    public Page<ChatDto> getAllByUser(Long userId, Pageable pageable) {
        Page<Chat> chats = chatRepository.findAllMine(userId, pageable);
        return chats.map(chat -> {
            ChatDto dto;
            boolean user1BlockedUser2 = userService.hasBlocked(chat.getUser1().getId(), chat.getUser2().getId());
            boolean user2BlockedUser1 = userService.hasBlocked(chat.getUser2().getId(), chat.getUser1().getId());
            if (user1BlockedUser2 || user2BlockedUser1)
                dto = ChatMapper.toBlockedDto(chat, user1BlockedUser2, user2BlockedUser1);
            else
                dto = ChatMapper.toDto(chat);
            return dto;
        });
    }

    @Transactional
    public ChatDto sendMessage(long chatId, ChatMessage message) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        chat.getMessages().add(message);
        chat.setSentAt(message.getSentAt());
        if  (chat.getUser1().getId() == message.getToUser().getId()) {
            chat.setStatus(ChatStatus.UNSEEN1);
        }
        else chat.setStatus(ChatStatus.UNSEEN2);
        return ChatMapper.toDto(chatRepository.save(chat));
    }
}
