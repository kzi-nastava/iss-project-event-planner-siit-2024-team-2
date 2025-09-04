package com.example.eventplanner.controllers.communication;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.communication.chatmessage.ChatMessageDto;
import com.example.eventplanner.dto.communication.chatmessage.ChatMessageNoIdDto;
import com.example.eventplanner.dto.communication.notification.NotificationDto;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.services.communication.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController()
@RequestMapping("/api/chat-messages")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final AuthUtil authUtil;

    @GetMapping("/{id}")
    public ResponseEntity<ChatMessageDto> getChatMessageById(@PathVariable Long id) {
        ChatMessageDto result = chatMessageService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<ChatMessageDto> createChatMessage(@RequestBody ChatMessageNoIdDto dto) {
        ChatMessageDto result = chatMessageService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatMessageDto> updateMessageText(@PathVariable Long id, @RequestBody String text) {
        ChatMessageDto result = chatMessageService.updateText(id,  text);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/seen")
    public ResponseEntity<Void> seenMessages(@RequestBody Collection<Long> ids) {
        Long userId = authUtil.getAuthenticatedUserId();
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            chatMessageService.seen(ids, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ChatMessageDto> deleteChatMessage(@PathVariable("id") Long id) {
        boolean success = chatMessageService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody ChatMessageNoIdDto dto) {
        return new ResponseEntity<>(chatMessageService.sendMessage(dto), HttpStatus.CREATED);
    }
}
