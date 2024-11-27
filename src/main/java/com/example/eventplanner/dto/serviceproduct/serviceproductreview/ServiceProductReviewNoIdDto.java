package com.example.eventplanner.dto.serviceproduct.serviceproductreview;

import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductReviewNoIdDto {
    private int grade;
    private String comment;
    private long userId;
    private long serviceProductId;
    private ReviewStatus reviewStatus;
}