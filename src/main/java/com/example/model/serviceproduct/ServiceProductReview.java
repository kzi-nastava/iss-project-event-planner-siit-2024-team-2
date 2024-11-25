package com.example.model.serviceproduct;

import com.example.model.Entity;
import com.example.model.user.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductReview extends Entity {
    private int grade;
    private String comment;
    private ServiceProduct serviceProduct;
    private User user;
}
