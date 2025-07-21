package com.example.eventplanner.model.event;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    @OneToMany(cascade = {CascadeType.ALL})
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
