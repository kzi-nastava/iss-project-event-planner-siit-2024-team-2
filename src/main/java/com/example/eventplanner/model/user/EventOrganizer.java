package com.example.eventplanner.model.user;

import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@SQLRestriction("active = true")
@Entity
public class EventOrganizer extends BaseUser {
}
