package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PriceListService {
    private final ServiceProductRepository serviceProductRepository;

    public Collection<PriceListDto> getBySppId(Long sppId) {
        return serviceProductRepository.getPriceListBySppId(sppId);
    }

    public PriceListDto update(Long id, Double price, Double discount) {
        return serviceProductRepository.findById(id)
                .map(sp -> {
                    sp.setPrice(price);
                    sp.setDiscount(discount);
                    ServiceProduct savedSp = serviceProductRepository.save(sp);
                    return ServiceProductMapper.toPriceListItemDto(savedSp);
                })
                .orElse(null);
    }
}
