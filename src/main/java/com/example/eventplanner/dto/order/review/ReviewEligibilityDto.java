package com.example.eventplanner.dto.order.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEligibilityDto {
    private boolean canReview;
    private String reason;
}
