package com.example.eventplanner.controllers.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductcategory.ServiceProductCategoryNoIdDto;
import com.example.eventplanner.model.serviceproduct.ServiceProductCategory;
import com.example.eventplanner.services.serviceproduct.ServiceProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/sp-categories")
@RequiredArgsConstructor()
public class ServiceProductCategoryController {
    private final ServiceProductCategoryService serviceProductCategoryService;

    @GetMapping()
    public ResponseEntity<Collection<ServiceProductCategoryDto>> getAllServiceProductCategories() {
        return ResponseEntity.ok(serviceProductCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProductCategoryDto> getServiceProductCategoryByName(@PathVariable long id) {
        ServiceProductCategoryDto dto = serviceProductCategoryService.getById(id);
        return dto != null ?
                new ResponseEntity<>(dto, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<ServiceProductCategoryDto> createServiceProductCategory(@RequestBody ServiceProductCategoryNoIdDto dto) {
        return new ResponseEntity<>(serviceProductCategoryService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceProductCategoryDto> updateServiceProductCategory(@PathVariable long id,
                                                                               @RequestBody ServiceProductCategoryNoIdDto dto) {
        ServiceProductCategoryDto updatedDto = serviceProductCategoryService.update(id, dto);
        return updatedDto != null ?
                new ResponseEntity<>(updatedDto, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceProductCategory> deleteServiceProductCategory(@PathVariable long id) {
        boolean success = serviceProductCategoryService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
