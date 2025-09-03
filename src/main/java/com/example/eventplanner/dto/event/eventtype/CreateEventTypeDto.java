package com.example.eventplanner.dto.event.eventtype;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventTypeDto {
    @NotBlank(message = "Name is required")
    @Length(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    public String name;
    @NotBlank(message = "Description is required")
    @Length(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    public String description;
    public List<Long> recommendedServiceProducts;
}
