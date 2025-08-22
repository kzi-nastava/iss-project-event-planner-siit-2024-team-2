package com.example.eventplanner.model.serviceproduct;

import com.example.eventplanner.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@jakarta.persistence.Entity
public class ServiceProductCategory extends Entity {
    private String name;
    private String description;
}
