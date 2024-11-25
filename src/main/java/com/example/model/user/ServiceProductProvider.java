package com.example.model.user;

import com.example.model.user.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductProvider extends User {
    private String companyName;
    private String companyDescription;
}
