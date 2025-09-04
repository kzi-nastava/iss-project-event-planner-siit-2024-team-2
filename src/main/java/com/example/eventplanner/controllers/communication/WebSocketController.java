package com.example.eventplanner.controllers.communication;

import com.example.eventplanner.dto.communication.MessageDto;
import com.example.eventplanner.services.communication.WebSocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final WebSocketService webSocketService;

    // Both of the endpoints will send the message to the publisher and subscriber if toId isn't null
    // Else, it behaves like a broadcast message (sent to all)
    // REST endpoint
    @RequestMapping(value="/send-message-rest", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto message) {
        if (webSocketService.trySend(message))
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // WebSockets endpoint
    @MessageMapping("/send/message")
    public MessageDto broadcastNotification(MessageDto message) {
        webSocketService.trySend(message);
        return message;
    }
}
