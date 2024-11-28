package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.pricelist.CreatePriceListDto;
import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PriceListService {
    private final static AtomicLong counter = new AtomicLong();
    private final HashMap<Long, PriceListDto> priceList = new HashMap<>();

    public Collection<PriceListDto> getAll() {
        return priceList.values();
    }

    public PriceListDto getById(Long id) {
        return priceList.get(id);
    }

    public PriceListDto create(CreatePriceListDto createPriceListDto) {
        Long id = counter.incrementAndGet();
        PriceListDto priceListDto = new PriceListDto(id, createPriceListDto);
        priceList.put(id, priceListDto);
        return priceListDto;
    }

    public PriceListDto update(Long id, CreatePriceListDto createPriceListDto) {
        if (this.getById(id) == null) {
            return null;
        }
        PriceListDto updatedPriceListDto = new PriceListDto(id, createPriceListDto);
        priceList.put(id, updatedPriceListDto);
        return updatedPriceListDto;
    }
}
