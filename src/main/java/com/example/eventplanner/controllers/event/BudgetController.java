package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.budget.BudgetDto;
import com.example.eventplanner.dto.event.budget.BudgetNoIdDto;
import com.example.eventplanner.dto.order.booking.BookingDto;
import com.example.eventplanner.dto.order.booking.BookingNoIdDto;
import com.example.eventplanner.dto.order.purchase.PurchaseNoIdDto;
import com.example.eventplanner.services.event.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor()
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
    public ResponseEntity<BudgetDto> createBudget(@RequestBody BudgetNoIdDto dto) {
        BudgetDto budgetDto = budgetService.create(dto);
        return new ResponseEntity<>(budgetDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> setNewAmount(@PathVariable("id") Long id, @RequestBody Double newAmount) {
        budgetService.setNewAmount(id, newAmount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/bookings")
    public ResponseEntity<?> addNewBooking(@PathVariable("id") Long id, @RequestBody BookingNoIdDto bookingDto) {
        budgetService.addBookingToBudget(id, bookingDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/purchases")
    public ResponseEntity<?> addNewPurchase(@PathVariable("id") Long id, @RequestBody PurchaseNoIdDto purchaseDto) {
        budgetService.addPurchaseToBudget(id, purchaseDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetDto> deleteBudget(@PathVariable("id") Long id) {
        boolean success = budgetService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
