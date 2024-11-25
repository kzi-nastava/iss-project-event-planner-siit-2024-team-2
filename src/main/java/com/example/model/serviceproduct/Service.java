package com.example.model.serviceproduct;

import com.example.model.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service extends Entity {
    private int duration;
}
