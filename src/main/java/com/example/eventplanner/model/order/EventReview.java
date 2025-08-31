package com.example.eventplanner.model.order;

import com.example.eventplanner.model.event.Event;
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
public class EventReview extends Review {
    @ManyToOne
    private Event event;
    public EventReview(Review review, Event event) {
        super(review.getGrade(), review.getComment(), review.getUser(), review.getReviewStatus(), review.getCreatedAt());
        this.event = event;
    }
}
