package com.example.eventplanner.dto.serviceproduct.serviceproductreview;

import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import com.example.eventplanner.dto.user.user.BaseUserDto;
import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductReviewDto {
    private long id;
    private int grade;
    private String comment;
    private ServiceProductDto serviceProduct;
    private BaseUserDto user;
    private ReviewStatus reviewStatus;
    private Instant createdAt;
}