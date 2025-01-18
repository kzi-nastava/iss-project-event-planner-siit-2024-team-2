package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.pricelist.CreatePriceListDto;
import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.services.serviceproduct.PriceListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return priceListDto != null ?
                ResponseEntity.ok(priceListDto) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<PriceListDto> createPriceList(@RequestBody CreatePriceListDto createPriceListDto) {
        return new ResponseEntity<>(priceListService.create(createPriceListDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PriceListDto> updatePriceList(@PathVariable("id") Long id, @RequestBody CreatePriceListDto createPriceListDto) {
        PriceListDto updatedPriceListDto = priceListService.update(id, createPriceListDto);
        return updatedPriceListDto != null ?
                ResponseEntity.ok(updatedPriceListDto) :
                ResponseEntity.notFound().build();
    }
}
