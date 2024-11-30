package com.example.eventplanner.model.serviceproduct;

import com.example.eventplanner.model.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class ServiceProductCategory extends Entity {
    private String name;
}
