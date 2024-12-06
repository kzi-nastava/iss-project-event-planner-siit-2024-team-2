package com.example.eventplanner.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SoftDelete(columnName = "isActive", strategy = SoftDeleteType.ACTIVE)
@SQLRestriction("isActive = true")
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private boolean isActive = true;
    public Entity withId(long id) {
        this.id = id;
        return this;
    }
    public boolean isActive() {
        return true;
    }
    public void setActive(boolean active) {}
}
