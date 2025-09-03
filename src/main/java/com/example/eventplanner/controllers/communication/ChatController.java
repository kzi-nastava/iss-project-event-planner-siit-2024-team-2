package com.example.eventplanner.controllers.communication;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.communication.chat.ChatDto;
import com.example.eventplanner.dto.communication.chat.ChatNoIdDto;
import com.example.eventplanner.dto.communication.chatmessage.ChatMessageDto;
import com.example.eventplanner.dto.communication.chatmessage.ChatMessageNoIdDto;
import com.example.eventplanner.dto.communication.notification.NotificationDto;
import com.example.eventplanner.model.communication.ChatMessage;
import com.example.eventplanner.services.communication.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final AuthUtil authUtil;

    @GetMapping("/mine")
    public ResponseEntity<Page<ChatDto>> getAllMyChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        Long userId = authUtil.getAuthenticatedUserId();
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sentAt"));
        Page<ChatDto> result = chatService.getAllByUser(userId, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatDto> getById(@PathVariable("id") Long id) {
        ChatDto chatDto = chatService.getById(id);
        return chatDto != null ?
                new ResponseEntity<>(chatDto, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/mine-and/{id2}")
    public ResponseEntity<Optional<ChatDto>> getMineAndUser2Id(@PathVariable("id2") Long id2) {
        Long myId = authUtil.getAuthenticatedUserId();
        if (myId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Optional<ChatDto> chatDto = chatService.getByUser1AndUser2(myId, id2);
        return new ResponseEntity<>(chatDto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ChatDto> createChat(@RequestBody ChatNoIdDto dto) {
        Long myId = authUtil.getAuthenticatedUserId();
        ChatDto chat = chatService.create(myId, dto);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PutMapping("{id}/send-message")
    public ResponseEntity<ChatDto> sendMessage(@PathVariable("id") long id, @RequestBody ChatMessage message) {
        return new ResponseEntity<>(chatService.sendMessage(id, message), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChatDto> deleteById(@PathVariable("id") Long id) {
        boolean success = chatService.delete(id);
        return new ResponseEntity<>(success ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
