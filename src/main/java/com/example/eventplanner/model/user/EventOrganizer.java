package com.example.eventplanner.model.user;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EventOrganizer extends User {
    @ManyToMany
    private List<ServiceProduct> favoriteServiceProducts;
}
