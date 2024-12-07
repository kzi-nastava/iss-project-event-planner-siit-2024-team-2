package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

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
    private Date dateApproved;
    private String reason;
}
