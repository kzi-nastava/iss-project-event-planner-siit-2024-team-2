package com.example.eventplanner.model.serviceproduct;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.user.ServiceProductProvider;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ServiceProduct extends Entity {
    @ManyToOne
    private ServiceProductCategory category;
    private boolean available;
    private boolean visible;
    private double price;
    private double discount;
    private String name;
    private String description;
    @ElementCollection
    @CollectionTable(name = "serviceproduct_image", joinColumns = @JoinColumn(name = "serviceproduct_id"))
    @Column(name = "image_path")
    private List<String> images;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<EventType> availableEventTypes;
    @ManyToOne
    private ServiceProductProvider serviceProductProvider;
}
