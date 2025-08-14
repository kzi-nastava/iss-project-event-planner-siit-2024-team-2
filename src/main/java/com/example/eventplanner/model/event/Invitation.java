package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class Invitation extends Entity {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private String email;
    private boolean registeredUser;
    private boolean accepted;
    private String token;

    public Invitation(Event event, String email) {
        super();
        this.event = event;
        this.email = email;
        this.token = UUID.randomUUID().toString();
    }
}
