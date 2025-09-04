package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.dto.user.user.BaseUserDto;
import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSummaryDto {
    private double grade;
    private String comment;
    private Instant createdAt;
    private String creatorName;
    private String creatorEmail;
    private String creatorProfilePicture;
}
