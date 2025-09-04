package com.example.eventplanner.model.communication;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.BaseUser;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class ChatMessage extends Entity {
    @Column(columnDefinition = "TEXT")
    private String text;
    private Instant sentAt;
    @ColumnDefault("false")
    private boolean seen;
    @ManyToOne
    private BaseUser toUser;

    public ChatMessage(String text, boolean seen, BaseUser toUser) {
        this.text = text;
        this.seen = seen;
        this.toUser = toUser;
        this.sentAt = Instant.now();
    }
}
