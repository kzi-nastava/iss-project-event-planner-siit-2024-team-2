package com.example.eventplanner.dto.communication;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String toId;
    private String fromId;
    private String message;
    private String title;
    private String topic = "";
    private String subtopic = "";
    private Instant timestamp = Instant.now();
}
