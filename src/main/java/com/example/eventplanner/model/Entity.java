package com.example.eventplanner.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SQLRestriction("active = true")
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ColumnDefault("true")
    private boolean active = true;
    public Entity withId(long id) {
        this.id = id;
        return this;
    }
}
