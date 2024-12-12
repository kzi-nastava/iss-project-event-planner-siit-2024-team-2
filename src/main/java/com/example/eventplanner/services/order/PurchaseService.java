package com.example.eventplanner.services.order;

import com.example.eventplanner.dto.order.booking.BookingMapper;
import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.dto.order.purchase.PurchaseMapper;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Booking;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Service;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.order.PurchaseRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
@Getter
@Setter
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final EventRepository eventRepository;

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
        Purchase purchase = PurchaseMapper.toEntity(dto, event, null);
        return PurchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    public PurchaseDto update(PurchaseNoIdDto dto, long id) {
        Purchase purchase = purchaseRepository.findById(id).orElse(null);
        if (purchase == null)
            return null;

        Event event = eventRepository.getReferenceById(dto.getEventId());
//        Service service = serviceRepository.getReferenceById(dto.getServiceId());

        purchase.setEvent(event);
//        purchase.setService(service);
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
