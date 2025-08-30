package com.example.eventplanner.dto.serviceproduct.service;

import com.example.eventplanner.dto.event.eventtype.EventTypeDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.dto.user.user.RegisterServiceProductProviderDto;
import com.example.eventplanner.dto.user.user.ServiceProductProviderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
	private Long id;
    private ServiceProductCategoryDto category;
    private boolean available;
    private boolean visible;
    private double price;
    private double discount;
    private String name;
    private String description;
    private List<String> images;
    private List<String> imageEncodedNames;
    private List<EventTypeDto> availableEventTypes;
    private ServiceProductProviderDto serviceProductProvider;

    private String specifies;
    private float duration;
    private float minEngagementDuration;
    private float maxEngagementDuration;
    private int reservationDaysDeadline;
    private int cancellationDaysDeadline;
    private boolean automaticReserved;
}
