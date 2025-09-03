package com.example.eventplanner.model.communication;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ChatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"})
)
public class Chat extends Entity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    private BaseUser user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id")
    private BaseUser user2;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChatStatus status;  // who got new (unseen) messages

    private Instant sentAt;
}
