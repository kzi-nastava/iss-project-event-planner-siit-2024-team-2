package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.EventReview;
import com.example.eventplanner.model.order.Review;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.order.ServiceProductReview;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ReviewType;

import java.time.Instant;

public class ReviewMapper {
    private ReviewMapper() {}

    public static ReviewDto toDto(Review review) {
        if (review == null)
            return null;

        ReviewDto dto = new ReviewDto(
                review.getId(),
                review.getGrade(),
                review.getComment(),
                UserMapper.toBaseUserDto(review.getUser()),
                review.getReviewStatus(),
                review.getCreatedAt()
        );

        if (review instanceof ServiceProductReview) {
            System.out.println("It's ServiceProductReview");
            return new ServiceProductReviewDto(
                    dto,
                    ServiceProductMapper.toDto(((ServiceProductReview) review).getServiceProduct())
            );
        } else if (review instanceof EventReview) {
            return new EventReviewDto(
                    dto,
                    EventMapper.toDto(((EventReview) review).getEvent())
            );
        } else
            return dto;
    }

    public static Review toEntity(ReviewNoIdDto dto,
                                    ServiceProduct serviceProduct,
                                    Event event,
                                    BaseUser user) {
        if (dto == null)
            return null;
        Review review = new Review(
                dto.getGrade(),
                dto.getComment(),
                user,
                dto.getReviewStatus(),
                Instant.now()
        );

        if (dto.getReviewType() == ReviewType.SERVICE_PRODUCT)
            return new ServiceProductReview(review, serviceProduct);
        else if (dto.getReviewType() == ReviewType.EVENT)
            return new EventReview(review, event);
        else
            return review;
    }
}