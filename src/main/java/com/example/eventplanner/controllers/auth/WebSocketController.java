package com.example.eventplanner.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Map;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Both of the endpoints will send the message to the publisher and subscriber if toId isn't null
    // Else, it behaves like a broadcast message (sent to all)
    // REST endpoint
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value="/send-message-rest", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> message) {
        if (message.containsKey("message")) {
            if (message.containsKey("toId") && message.get("toId") != null && !message.get("toId").equals("")) {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("toId"), message);
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("fromId"), message);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher", message);
            }
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // WebSockets endpoint
    @MessageMapping("/send/message")
    public Map<String, String> broadcastNotification(String message) {
        Map<String, String> messageConverted = parseMessage(message);

        if (messageConverted != null) {
            if (messageConverted.containsKey("toId") && messageConverted.get("toId") != null
                    && !messageConverted.get("toId").equals("")) {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + messageConverted.get("toId"),
                        messageConverted);
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + messageConverted.get("fromId"),
                        messageConverted);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher", messageConverted);
            }
        }

        return messageConverted;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> retVal;

        try {
            retVal = mapper.readValue(message, Map.class); // JSON string parsing
        } catch (IOException e) {
            retVal = null;
        }

        return retVal;
    }
}
