package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.pricelist.PriceListDto;
import com.example.eventplanner.services.serviceproduct.PriceListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/price-list")
@RequiredArgsConstructor()
public class PriceListController {
    private final PriceListService priceListService;

    @GetMapping(value = "/{sppId}")
    public ResponseEntity<Collection<PriceListDto>> getBySppId(@PathVariable("sppId") Long sppId) {
        Collection<PriceListDto> priceList = priceListService.getBySppId(sppId);
        return priceList != null ?
                ResponseEntity.ok(priceList) :
                ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PriceListDto> updatePriceList(@PathVariable("id") Long id,
                                                        @RequestParam Double price,
                                                        @RequestParam Double discount) {
        PriceListDto updatedPriceListDto = priceListService.update(id, price, discount);
        return updatedPriceListDto != null ?
                ResponseEntity.ok(updatedPriceListDto) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        byte[] pdf = priceListService.generatePdf (id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=price_list_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
