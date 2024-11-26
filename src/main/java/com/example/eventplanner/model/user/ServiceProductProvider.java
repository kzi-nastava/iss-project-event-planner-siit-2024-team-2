package com.example.eventplanner.model.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductProvider extends User {
    private String companyName;
    private String companyDescription;
}
