package com.example.eventplanner.dto.order.purchase;

import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.dto.serviceproduct.product.ProductMapper;
import com.example.eventplanner.model.order.Purchase;

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
                EventMapper.toDto(purchase.getEvent()),
                ProductMapper.toDto(purchase.getProduct()),
                purchase.getPrice()
        );
    }

    public static Purchase toEntity(PurchaseDto dto, int depth) {
        if (dto == null || depth > 1)
            return null;
        return (Purchase) new Purchase(
                EventMapper.toEntity(dto.getEvent(), depth + 1),
                ProductMapper.toEntity(dto.getProduct(), depth + 1),
                dto.getPrice()).withId(dto.getId());
    }

    public static Purchase toEntity(PurchaseNoIdDto dto, int depth) {
        if (dto == null || depth > 1)
            return null;
        return new Purchase(
                EventMapper.toEntity(dto.getEvent(), depth + 1),
                ProductMapper.toEntity(dto.getProduct(), depth + 1),
                dto.getPrice());
    }
}