package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.serviceproduct.pricelist.CreatePriceListDto;
import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.services.serviceproduct.PriceListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/price-list")
@RequiredArgsConstructor()
public class PriceListController {
    private final PriceListService priceListService;

    @GetMapping()
    public ResponseEntity<Collection<PriceListDto>> getAllPriceList() {
        return ResponseEntity.ok(priceListService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PriceListDto> getPriceListById(@PathVariable("id") Long id) {
        PriceListDto priceListDto = priceListService.getById(id);
        if (priceListDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(priceListDto);
    }

    @PostMapping()
    public ResponseEntity<PriceListDto> createPriceList(@RequestBody CreatePriceListDto createPriceListDto) {
        return ResponseEntity.ok(priceListService.create(createPriceListDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PriceListDto> updatePriceList(@PathVariable("id") Long id, @RequestBody CreatePriceListDto createPriceListDto) {
        PriceListDto updatedPriceListDto = priceListService.update(id, createPriceListDto);
        if (updatedPriceListDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPriceListDto);
    }
}
