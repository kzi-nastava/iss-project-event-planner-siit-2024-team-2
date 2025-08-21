package com.example.eventplanner.model.communication;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.BaseUser;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class Notification extends Entity {
    private String title;
    @Column(columnDefinition = "TEXT")
    private String message;
    private Instant sentAt;
    private boolean seen;
    private boolean dismissed;
    @ManyToOne
    private BaseUser user;

    public Notification(String title, String message, boolean seen, boolean dismissed, BaseUser user) {
        this.title = title;
        this.message = message;
        this.seen = seen;
        this.dismissed = dismissed;
        this.user = user;
        this.sentAt = Instant.now();
    }
}
