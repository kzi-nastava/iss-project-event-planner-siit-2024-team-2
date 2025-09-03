package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductNoIdDto {
    @Min(value = 1, message = "Category is required")
    private long categoryId;
    private boolean available;
    private boolean visible;
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;
    @Min(value = 0, message = "Discount must be greater or equal to 0")
    private double discount;
    @NotBlank(message = "Name is required")
    @Length(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Length(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    private String description;
    @NotEmpty(message = "At least one image is required")
    private List<String> images;
    private List<Long> availableEventTypeIds;
    @Min(value = 1, message = "Service product provider is required")
    private long serviceProductProviderId;
    private String dtype;
}
