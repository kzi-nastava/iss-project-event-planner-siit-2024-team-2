package com.example.eventplanner.model.serviceproduct;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ReviewStatus;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class ServiceProductReview extends Entity {
    private int grade;
    @Column(columnDefinition = "TEXT")
    private String comment;
    @ManyToOne
    private ServiceProduct serviceProduct;
    @ManyToOne
    private BaseUser user;
    private ReviewStatus reviewStatus;
    private Instant createdAt;
}
