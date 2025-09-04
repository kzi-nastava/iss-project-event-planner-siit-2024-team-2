package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.user.user.BaseUserDto;
import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private long id;
    private double grade;
    private String comment;
    private BaseUserDto user;
    private ReviewStatus reviewStatus;
    private Instant createdAt;
}