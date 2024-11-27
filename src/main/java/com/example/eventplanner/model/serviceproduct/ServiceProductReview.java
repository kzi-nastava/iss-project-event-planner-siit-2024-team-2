package com.example.eventplanner.model.serviceproduct;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.ReviewStatus;
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
    private ReviewStatus reviewStatus;
}
