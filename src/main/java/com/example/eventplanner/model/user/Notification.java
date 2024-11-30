package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class Notification extends Entity {
    private String message;
    private Date dateSent;
    private boolean seen;
    @ManyToOne
    private User user;
}
