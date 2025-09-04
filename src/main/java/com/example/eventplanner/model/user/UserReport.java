package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
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
public class UserReport extends Entity {
    @ManyToOne
    private BaseUser reporter;
    @ManyToOne
    private BaseUser reported;
    private Instant approvedAt = null;
    @Column(columnDefinition = "TEXT")
    private String reason;
    private final Instant createdAt = Instant.now();
}
