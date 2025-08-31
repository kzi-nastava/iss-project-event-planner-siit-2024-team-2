package com.example.eventplanner.model.order;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class ServiceProductReview extends Review {
    @ManyToOne
    private ServiceProduct serviceProduct;
    public ServiceProductReview(Review review, ServiceProduct serviceProduct) {
        super(review.getGrade(), review.getComment(), review.getUser(), review.getReviewStatus(), review.getCreatedAt());
        this.serviceProduct = serviceProduct;
    }
}
