package com.example.eventplanner.model.order;

import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.serviceproduct.Service;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@SQLDelete(sql
        = "UPDATE booking "
        + "SET isactive = false "
        + "WHERE id = ?")
@SQLRestriction("isActive = true")
public class Booking extends Entity {
    @ManyToOne
    private Event event;
    @ManyToOne
    private Service service;
    private double price;
    private Date date;
    private double duration;
}
