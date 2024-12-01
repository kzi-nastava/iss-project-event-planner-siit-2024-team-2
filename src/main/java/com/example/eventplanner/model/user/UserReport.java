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
public class UserReport extends Entity {
    @ManyToOne
    private BaseUser reporter;
    @ManyToOne
    private BaseUser reported;
    private Date dateApproved;
    private String reason;
}
