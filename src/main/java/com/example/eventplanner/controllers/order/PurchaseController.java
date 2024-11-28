package com.example.eventplanner.controllers.order;

import com.example.eventplanner.dto.order.purchase.PurchaseDto;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.services.order.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor()
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<Collection<PurchaseDto>> getPurchases() {
        Collection<PurchaseDto> result = purchaseService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PurchaseDto> getPurchaseById(@PathVariable("id") Long id) {
        PurchaseDto result = purchaseService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<PurchaseDto> createPurchase(@RequestBody PurchaseNoIdDto dto) {
        PurchaseDto result = purchaseService.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PurchaseDto> updatePurchase(@PathVariable("id") Long id, @RequestBody PurchaseNoIdDto dto) {
        PurchaseDto result = purchaseService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<PurchaseDto> deletePurchase(@PathVariable("id") Long id) {
        boolean success = purchaseService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
