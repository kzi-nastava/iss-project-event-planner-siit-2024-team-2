package com.example.eventplanner.model.serviceproduct;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.ReviewStatus;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class ServiceProductReview extends Entity {
    private int grade;
    private String comment;
    @ManyToOne
    private ServiceProduct serviceProduct;
    @ManyToOne
    private User user;
    private ReviewStatus reviewStatus;
}
