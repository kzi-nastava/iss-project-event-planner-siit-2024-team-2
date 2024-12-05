package com.example.eventplanner.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SQLDelete(sql
        = "UPDATE entity "
        + "SET isactive = false "
        + "WHERE id = ?")
@SQLRestriction("isActive = true")
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean isActive = true;
    public Entity withId(long id) {
        this.id = id;
        return this;
    }
}
