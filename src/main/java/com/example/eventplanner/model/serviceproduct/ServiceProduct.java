package com.example.eventplanner.model.serviceproduct;

import java.util.List;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.user.ServiceProductProvider;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
    @CollectionTable(name = "user_images", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "image_path")
    private List<String> images;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<EventType> availableEventTypes;
    @ManyToOne(cascade = {CascadeType.ALL})
    private ServiceProductProvider serviceProductProvider;
}
