package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.model.utils.ReviewType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewNoIdDto {
    private int grade;
    private String comment;
    private long userId;
    private ReviewStatus reviewStatus;
    private long entityId;
    private ReviewType reviewType;
}