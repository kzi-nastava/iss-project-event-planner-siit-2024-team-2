package com.example.model.user;

import com.example.model.serviceproduct.ServiceProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventOrganizer extends User {
    private List<ServiceProduct> favoriteServiceProducts;
}
