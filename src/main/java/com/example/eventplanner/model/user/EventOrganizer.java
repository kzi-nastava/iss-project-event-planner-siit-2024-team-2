package com.example.eventplanner.model.user;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("active = true")
@Entity
public class EventOrganizer extends BaseUser {
    @ManyToMany
    private List<ServiceProduct> favoriteServiceProducts;
}
