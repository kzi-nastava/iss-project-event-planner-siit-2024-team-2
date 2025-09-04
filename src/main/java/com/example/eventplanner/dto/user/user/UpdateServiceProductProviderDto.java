package com.example.eventplanner.dto.user.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceProductProviderDto {
    @Length(max = 100, message = "First name must be max 100 characters")
    private String firstName;
    @Length(max = 100, message = "Last name must be max 100 characters")
    private String lastName;
    @Length(max = 100, message = "Address must be max 100 characters")
    private String address;
    @Length(max = 100, message = "Phone number must be max 100 characters")
    private String phoneNumber;
    @Length(max = 1000, message = "Company description must be max 1000 characters")
    private String companyDescription;
}
