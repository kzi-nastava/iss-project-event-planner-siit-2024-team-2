package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.model.utils.ReviewType;
import com.example.eventplanner.validators.ValidRating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewNoIdDto {
    @ValidRating
    private double grade;
    @NotEmpty
    @Size(min = 1, max = 1000)
    private String comment;
    @NotEmpty
    private long userId;
    @NotEmpty
    private long entityId;
    @NotEmpty
    private ReviewType reviewType;
}