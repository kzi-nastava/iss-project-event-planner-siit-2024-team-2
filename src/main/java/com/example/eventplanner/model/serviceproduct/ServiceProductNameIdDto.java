package com.example.eventplanner.model.serviceproduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductNameIdDto {
    private long id;
    private String name;
    private String Description;
}
