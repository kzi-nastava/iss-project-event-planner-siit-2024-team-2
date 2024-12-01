package com.example.eventplanner.model.user;

import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ServiceProductProvider extends BaseUser {
    private String companyName;
    private String companyDescription;
    @ManyToMany
    private List<ServiceProductCategory> serviceProductCategory = new ArrayList<>();
}
