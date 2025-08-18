package com.example.eventplanner.services.communication;

import com.example.eventplanner.dto.communication.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public boolean trySend(MessageDto message) {
        if (message.getMessage() == null || message.getMessage().isEmpty())
            return false;

        String destination = "/socket-publisher/";
        if (message.getTopic() != null && !message.getTopic().isEmpty()) {
            if (message.getSubtopic() != null && !message.getSubtopic().isEmpty())
                destination += message.getTopic() + "/" + message.getSubtopic();
            else
                destination += message.getTopic();
        }

        if (message.getToId() != null && !message.getToId().isEmpty()) {
            this.simpMessagingTemplate.convertAndSend(destination + "/" + message.getToId(), message);
            if (message.getFromId() != null && !message.getFromId().isEmpty())
                this.simpMessagingTemplate.convertAndSend(destination + "/" + message.getFromId(), message);
        } else
            this.simpMessagingTemplate.convertAndSend(destination, message);
        System.out.println(destination);
        return true;
    }
}
