package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductSummaryDto {
    private long id;
    private ServiceProductCategoryDto category;
    private boolean available;
    private double price;
    private double discount;
    private String name;
    private String description;
    private String creatorName;
    private String creatorEmail;
}
