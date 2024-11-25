package com.example.model.event;

import com.example.model.Entity;
import com.example.model.user.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventReview extends Entity {
    private int grade;
    private String comment;
    private User user;
    private Event event;
}
