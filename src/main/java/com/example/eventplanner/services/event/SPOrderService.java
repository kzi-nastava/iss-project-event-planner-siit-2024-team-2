package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.SPOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class SPOrderService {
    List<SPOrder> orders = new ArrayList<>();
    public SPOrderService() {}
}
