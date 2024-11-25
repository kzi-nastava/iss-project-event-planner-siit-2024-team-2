package com.example.model.user;

import com.example.model.Entity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReport extends Entity {
    private User reporter;
    private User reported;
    private Date dateApproved;
}
