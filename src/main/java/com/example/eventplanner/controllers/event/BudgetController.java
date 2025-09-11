package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.budget.BudgetDto;
import com.example.eventplanner.dto.event.budget.BudgetNoIdDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.exception.NotFoundException;
import com.example.eventplanner.services.event.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor()
@Validated
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<Collection<BudgetDto>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetDto> getBudgetById(@PathVariable("id") Long id) {
        BudgetDto budgetDto = budgetService.getById(id);
        return budgetDto != null ? ResponseEntity.ok(budgetDto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<BudgetDto> createBudget(@Valid @RequestBody BudgetNoIdDto dto) {
        BudgetDto budgetDto = budgetService.create(dto);
        return new ResponseEntity<>(budgetDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> setNewAmount(@PathVariable("id") Long id, @RequestBody Double newAmount) {
        budgetService.setNewAmount(id, newAmount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/bookings")
    public ResponseEntity<Void> addNewBooking(@PathVariable("id") Long id, @Valid @RequestBody BookingNoIdDto bookingDto) {
        try {
            budgetService.addBookingToBudget(id, bookingDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{id}/purchases")
    public ResponseEntity<Void> addNewPurchase(@PathVariable("id") Long id, @Valid @RequestBody PurchaseNoIdDto purchaseDto) {
        try {
            budgetService.addPurchaseToBudget(id, purchaseDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // ❌ Wrong — still 200
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetDto> deleteBudget(@PathVariable("id") Long id) {
        boolean success = budgetService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
