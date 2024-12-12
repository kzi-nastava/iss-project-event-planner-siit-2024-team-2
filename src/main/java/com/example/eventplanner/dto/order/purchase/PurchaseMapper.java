package com.example.eventplanner.dto.order.purchase;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.serviceproduct.product.ProductMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Product;

public class PurchaseMapper {
    public static PurchaseDto toDto(Purchase purchase) {
        if (purchase == null)
            return null;
        return new PurchaseDto(
                purchase.getId(),
                EventMapper.toDto(purchase.getEvent()),
                ProductMapper.toDto(purchase.getProduct()),
                purchase.getPrice()
        );
    }

    public static PurchaseNoIdDto toDtoNoId(Purchase purchase) {
        if (purchase == null)
            return null;
        return new PurchaseNoIdDto(
                purchase.getEvent().getId(),
                purchase.getProduct().getId(),
                purchase.getPrice()
        );
    }

    public static Purchase toEntity(PurchaseNoIdDto dto, Event event, Product product) {
        if (dto == null)
            return null;
        return new Purchase(
                event,
                product,
                dto.getPrice());
    }
}