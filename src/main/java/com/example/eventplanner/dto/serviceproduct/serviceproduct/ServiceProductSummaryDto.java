package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.dto.user.user.ServiceProductProviderDto;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
    private ServiceProductProviderDto serviceProductProvider;
}
