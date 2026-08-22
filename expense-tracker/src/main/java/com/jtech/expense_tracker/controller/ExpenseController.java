package com.jtech.expense_tracker.controller;

import com.jtech.expense_tracker.dto.ExpenseRequest;
import com.jtech.expense_tracker.dto.ExpenseResponse;
import com.jtech.expense_tracker.entity.ExpenseCategory;
import com.jtech.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

//    @GetMapping
//    public Page<ExpenseResponse> getAllExpenses(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "asc") String direction) {
//
//        return service.getAllExpenses(page, size, sortBy, direction);
//    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request) {

        ExpenseResponse response =
                service.createExpense(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable Long id) {

        ExpenseResponse response =
                service.getExpenseById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {

        ExpenseResponse response =
                service.updateExpense(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id) {

        service.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public Page<ExpenseResponse> getExpensesByCategory(
            @PathVariable ExpenseCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
            ) {

        return service.getExpensesByCategory(
                category,
                page,
                size,
                sortBy,
                direction);
    }

    @GetMapping("/amount")
    public Page<ExpenseResponse> getExpensesByAmountRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
            ) {

        return service.getExpensesByAmountRange(
                min,
                max,
                page,
                size,
                sortBy,
                direction
        );
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> searchExpenses(

            @RequestParam(required = false)
            ExpenseCategory category,

            @RequestParam(required = false)
            BigDecimal minAmount,

            @RequestParam(required = false)
            BigDecimal maxAmount,

            @RequestParam(required = false)
            String title,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction) {

        Page<ExpenseResponse> response =
                service.searchExpenses(
                category,
                minAmount,
                maxAmount,
                title,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(response);

    }
}
