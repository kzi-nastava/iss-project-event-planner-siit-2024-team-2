package com.example.model.user;

import com.example.model.Entity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends Entity {
    private String message;
    private Date dateSent;
    private boolean seen;
    private User user;
}
