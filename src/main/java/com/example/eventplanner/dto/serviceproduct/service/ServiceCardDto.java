package com.example.eventplanner.dto.serviceproduct.service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCardDto {
    private Long id;
    private double price;
    private double discount;
    private String name;
    private String description;
    private String image;
}
