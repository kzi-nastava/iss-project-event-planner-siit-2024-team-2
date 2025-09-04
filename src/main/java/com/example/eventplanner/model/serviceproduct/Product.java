package com.example.eventplanner.model.serviceproduct;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("active = true")
@Entity
@DiscriminatorValue("Product")
public class Product extends ServiceProduct{
}
