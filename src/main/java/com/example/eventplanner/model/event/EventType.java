package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class EventType extends Entity {
    private String name;
    private String description;
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "availableEventTypes")
    private List<ServiceProduct> serviceProducts;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "eventtype_serviceproduct",
            joinColumns = @JoinColumn(name = "eventtype_id"),
            inverseJoinColumns = @JoinColumn(name = "recommendedserviceproducts_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"eventtype_id", "recommendedserviceproducts_id"})
    )
    private List<ServiceProduct> recommendedServiceProducts;
    public EventType(String name, List<ServiceProduct> recommendedServiceProducts) {
        this.name = name;
        this.recommendedServiceProducts = recommendedServiceProducts;
    }

    public EventType(String name, String description, List<ServiceProduct> recommendedServiceProducts) {
        this.name = name;
        this.description = description;
        this.recommendedServiceProducts = recommendedServiceProducts;
    }
}
