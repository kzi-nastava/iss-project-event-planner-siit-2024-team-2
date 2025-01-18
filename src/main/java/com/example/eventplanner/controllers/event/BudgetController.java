package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.budget.BudgetDto;
import com.example.eventplanner.dto.event.budget.BudgetNoIdDto;
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
    public ResponseEntity<BudgetDto> updateBudget(@PathVariable("id") Long id, @RequestBody BudgetNoIdDto dto) {
        BudgetDto budgetDto = budgetService.update(id, dto);
        return budgetDto != null ? ResponseEntity.ok(budgetDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetDto> deleteBudget(@PathVariable("id") Long id) {
        boolean success = budgetService.delete(id);
        return success ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
