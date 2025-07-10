package com.example.eventplanner.dto.serviceproduct.serviceproduct;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductFilteringValuesDto {
    private Double minPrice;
    private Double maxPrice;
    private Float minDuration;
    private Float maxDuration;
    private List<ServiceProductCategoryDto> categoryIds;
    private List<EventTypeDto> availableEventTypeIds;
}
