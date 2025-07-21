package com.example.eventplanner.dto.user.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
