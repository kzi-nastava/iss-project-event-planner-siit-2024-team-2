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
public class CompanyInfoDto {
    @Length(max = 100, message = "Company name must be max 100 characters")
    private String companyName;
    @Length(max = 1000, message = "Company description must be max 1000 characters")
    private String companyDescription;
}
