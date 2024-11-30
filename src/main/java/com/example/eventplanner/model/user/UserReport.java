package com.example.eventplanner.model.user;

import com.example.eventplanner.model.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class UserReport extends Entity {
    @ManyToOne
    private User reporter;
    @ManyToOne
    private User reported;
    private Date dateApproved;
    private String reason;
}
