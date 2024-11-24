package com.example.model.serviceproduct;

import com.example.model.user.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductReview {
    private int grade;
    private String comment;
    private ServiceProduct serviceProduct;
    private User user;
}
