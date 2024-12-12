package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.dto.order.purchase.PurchaseMapper;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.order.PurchaseRepository;
import com.example.eventplanner.repositories.serviceproduct.ProductRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

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
        Event event = eventRepository.getReferenceById(dto.getEventId());
        Product product = productRepository.getReferenceById(dto.getProductId());
        Purchase purchase = PurchaseMapper.toEntity(dto, event, product);
        return PurchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    public PurchaseDto update(PurchaseNoIdDto dto, long id) {
        Purchase purchase = purchaseRepository.findById(id).orElse(null);
        if (purchase == null)
            return null;

        Event event = eventRepository.getReferenceById(dto.getEventId());
        Product product = productRepository.getReferenceById(dto.getProductId());

        purchase.setEvent(event);
        purchase.setProduct(product);
        purchase.setPrice(dto.getPrice());
        return PurchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    public boolean delete(long id) {
        if (!purchaseRepository.existsById(id))
            return false;
        purchaseRepository.deleteById(id);
        return true;
    }
}
