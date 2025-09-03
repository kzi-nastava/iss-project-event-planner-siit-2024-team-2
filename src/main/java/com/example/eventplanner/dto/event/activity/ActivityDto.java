package com.example.eventplanner.dto.event.activity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    @NotBlank(message = "Name is required")
    @Length(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    @NotNull(message = "Activity start is required")
    private Long activityStart;
    @NotNull(message = "Activity end is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Activity end must be greater than 0")
    private Long activityEnd;
    @NotBlank(message = "Description is required")
    @Length(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    private String description;
    @NotBlank(message = "Location is required")
    @Length(min = 1, max = 100, message = "Location must be between 1 and 100 characters")
    private String location;
}
