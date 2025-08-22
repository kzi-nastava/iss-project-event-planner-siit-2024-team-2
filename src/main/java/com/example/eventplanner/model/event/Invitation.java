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
    private String token;
    private boolean accepted;
    private boolean quickRegistration;

    public Invitation(Event event, String email) {
        this(event, email, false);
    }

    public Invitation(Event event, String email, boolean quickRegistration) {
        super();
        this.event = event;
        this.email = email;
        this.token = UUID.randomUUID().toString();
        this.accepted = false;
        this.quickRegistration = quickRegistration;
    }
}
