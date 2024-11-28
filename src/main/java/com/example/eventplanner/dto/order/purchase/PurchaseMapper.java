package com.example.eventplanner.dto.order.purchase;

import com.example.eventplanner.model.order.Purchase;

public class PurchaseMapper {
    public static PurchaseDto toDto(Purchase purchase) {
        if (purchase == null)
            return null;
        return new PurchaseDto(
                purchase.getId(),
                purchase.getEvent().getId(),
                purchase.getProduct().getId(),
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

    public static Purchase toEntity(PurchaseDto dto) {
        if (dto == null)
            return null;
        Purchase purchase = new Purchase(
                null,
                null,
                dto.getPrice());
        purchase.setId(dto.getId());
        purchase.setActive(true);
        return purchase;
    }

    public static Purchase toEntity(PurchaseNoIdDto dto) {
        if (dto == null)
            return null;
        Purchase purchase = new Purchase(
                null,
                null,
                dto.getPrice());
        purchase.setActive(true);
        return purchase;
    }
}