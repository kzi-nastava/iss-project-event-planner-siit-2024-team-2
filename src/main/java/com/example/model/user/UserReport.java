package com.example.model.user;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReport {
    private User reporter;
    private User reported;
    private Date dateApproved;
}
