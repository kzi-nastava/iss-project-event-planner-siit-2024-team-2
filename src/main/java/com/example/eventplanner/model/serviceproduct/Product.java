package com.example.eventplanner.model.serviceproduct;

import jakarta.persistence.Entity;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("active = true")
@Entity
public class Product extends ServiceProduct{
}
