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
public class ServiceProductReviewStatusDto {
    private long id;
    private ReviewStatus reviewStatus;
}