package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.model.utils.ReviewType;
import com.example.eventplanner.validators.ValidRating;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewNoIdDto {
    @ValidRating(message = "Grade must be between 1 and 5, with a step of 0.5")
    private double grade;
    @NotBlank(message = "Comment is required")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String comment;
    @Min(value = 1, message = "Entity is required")
    private long entityId;
    @NotNull(message = "Review type is required")
    private ReviewType reviewType;
}