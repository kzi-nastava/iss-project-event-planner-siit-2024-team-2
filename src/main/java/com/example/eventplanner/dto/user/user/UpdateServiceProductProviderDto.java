package com.example.eventplanner.dto.user.user;

import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceProductProviderDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String companyDescription;
}
