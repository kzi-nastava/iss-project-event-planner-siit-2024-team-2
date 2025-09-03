package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.serviceproduct.product.CreateProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductDetailsDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductDto;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.services.serviceproduct.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;
    private final AuthUtil authUtil;

    @GetMapping()
    public ResponseEntity<Collection<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }
    @GetMapping("/mine")
    public ResponseEntity<Collection<ProductDto>> getProviderProducts() {
        ServiceProductProvider provider = authUtil.getAuthenticatedServiceProductProvider();
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ProductDto> products = productService.getAllByProviderId(provider.getId());
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDetailsDto> getProductById(@PathVariable("id") Long id) {
        ProductDetailsDto productDto = productService.getById(id);
        return productDto != null ?
                ResponseEntity.ok(productDto) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto productDto) {
        return new ResponseEntity<>(productService.create(productDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable("id") Long id, @Valid @RequestBody CreateProductDto productDto) {
        ProductDto updatedProductDto = productService.update(id, productDto);
        return updatedProductDto != null ?
                ResponseEntity.ok(updatedProductDto) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable("id") Long id) {
        boolean success = productService.delete(id);
        return success
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ProductDto>> searchProductsByName(@RequestParam("name") String name) {
        Collection<ProductDto> productDtos = productService.searchByName(name);
        return !productDtos.isEmpty() ?
                ResponseEntity.ok(productDtos) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }

    @GetMapping("/filter")
    public ResponseEntity<Collection<ProductDto>> filterProducts(@RequestParam(value = "category", required = false) Long category,
                                                                 @RequestParam(value = "eventTypes", required = false) List<Long> eventTypes,
                                                                 @RequestParam(value = "minPrice", required = false) Float minPrice,
                                                                 @RequestParam(value = "maxPrice", required = false) Float maxPrice,
                                                                 @RequestParam(value = "available", required = false) Boolean available) {
        List<ProductDto> productDtos = productService.filter(category, eventTypes, minPrice, maxPrice, available);
        return ResponseEntity.ok(productDtos);
    }
}