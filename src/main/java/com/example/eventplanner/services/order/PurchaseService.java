package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.dto.order.purchase.PurchaseMapper;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.repositories.order.PurchaseRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public List<PurchaseDto> getAll() {
        return purchaseRepository.findAll()
                .stream()
                .map(PurchaseMapper::toDto)
                .toList();
    }

    public PurchaseDto getById(long id) {
        return purchaseRepository.findById(id)
                .map(PurchaseMapper::toDto)
                .orElse(null);
    }

    public PurchaseDto create(PurchaseNoIdDto dto) {
        Purchase purchase = PurchaseMapper.toEntity(dto, 0);
        return PurchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    public PurchaseDto update(PurchaseNoIdDto dto, long id) {
        if (!purchaseRepository.existsById(id))
            return null;
        Purchase purchase = (Purchase) PurchaseMapper.toEntity(dto, 0).withId(id);
        return PurchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    public boolean delete(long id) {
        if (!purchaseRepository.existsById(id))
            return false;
        purchaseRepository.deleteById(id);
        return true;
    }
}
