package com.example.eventplanner.model.user;

import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductProvider extends User {
    private String companyName;
    private String companyDescription;
    private List<ServiceProductCategory> serviceProductCategory = new ArrayList<>();
}
