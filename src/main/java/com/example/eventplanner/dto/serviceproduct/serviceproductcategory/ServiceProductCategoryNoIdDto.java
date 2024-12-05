package com.example.eventplanner.dto.serviceproduct.serviceproductcategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductCategoryNoIdDto {
    private boolean isActive = true;
    private String name;
}
