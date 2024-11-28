package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.dto.order.purchase.PurchaseMapper;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
public class PurchaseService {
    Map<Long, Purchase> purchases = new HashMap<>();
    private long idCounter = 0;

    public List<PurchaseDto> getAll() {
        return purchases.values()
                .stream()
                .filter(Entity::isActive)
                .map(PurchaseMapper::toDto)
                .toList();
    }

    public PurchaseDto getById(long id) {
        if (!purchases.containsKey(id) || !purchases.get(id).isActive())
            return null;
        return PurchaseMapper.toDto(purchases.get(id));
    }

    public PurchaseDto create(PurchaseNoIdDto dto) {
        Purchase purchase = PurchaseMapper.toEntity(dto);
        purchase.setId(idCounter++);
        purchase.setActive(true);

        // link event
        Event testEvent = new Event();
        testEvent.setId(dto.getEventId());
        purchase.setEvent(testEvent);
        // link product
        Product testProduct = new Product();
        testProduct.setId(dto.getProductId());
        purchase.setProduct(testProduct);

        purchases.put(purchase.getId(), purchase);
        return PurchaseMapper.toDto(purchase);
    }

    public PurchaseDto update(PurchaseNoIdDto dto, long id) {
        if (this.getById(id) == null)
            return null;
        Purchase purchase = PurchaseMapper.toEntity(dto);

        // link event
        Event testEvent = new Event();
        testEvent.setId(dto.getEventId());
        purchase.setEvent(testEvent);
        // link product
        Product testProduct = new Product();
        testProduct.setId(dto.getProductId());
        purchase.setProduct(testProduct);

        purchases.put(id, PurchaseMapper.toEntity(dto));
        return PurchaseMapper.toDto(purchase);
    }

    public boolean delete(long id) {
        if (this.getById(id) == null)
            return false;
        purchases.get(id).setActive(false);
        return true;
    }
}
