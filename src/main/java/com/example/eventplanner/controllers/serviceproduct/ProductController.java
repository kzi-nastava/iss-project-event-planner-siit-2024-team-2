package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.product.CreateProductDto;
import com.example.eventplanner.dto.serviceproduct.product.ProductDto;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.services.serviceproduct.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<Collection<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        ProductDto productDto = productService.getById(id);

        return productDto != null ?
                ResponseEntity.ok(productDto) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductDto productDto) {
        return new ResponseEntity<>(productService.create(productDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody CreateProductDto productDto, @PathVariable("id") Long id) {
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
    public ResponseEntity<Collection<ProductDto>> filterProducts(@RequestParam(value = "categories", required = false) List<ServiceProductCategory> categories,
                                                                 @RequestParam(value = "eventTypes", required = false) List<String> eventTypes,
                                                                 @RequestParam(value = "minPrice", required = false) Float minPrice,
                                                                 @RequestParam(value = "maxPrice", required = false) Float maxPrice,
                                                                 @RequestParam(value = "available", required = false) Boolean available) {

        return ResponseEntity.ok(productService.filter(categories, eventTypes, minPrice, maxPrice, available));
    }
}