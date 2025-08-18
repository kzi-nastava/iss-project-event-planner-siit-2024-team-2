package com.example.eventplanner.dto.communication;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String toId;
    private String fromId;
    private String message;
    private String topic;
    private String subtopic;
    private Long timestamp;
}
