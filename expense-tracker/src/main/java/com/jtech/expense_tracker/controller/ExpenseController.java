package com.jtech.expense_tracker.controller;

import com.jtech.expense_tracker.dto.ExpenseRequest;
import com.jtech.expense_tracker.dto.ExpenseResponse;
import com.jtech.expense_tracker.entity.ExpenseCategory;
import com.jtech.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ExpenseResponse createExpense(@Valid @RequestBody ExpenseRequest request) {

        return service.createExpense(request);
    }

    @GetMapping("/{id}")
    public ExpenseResponse getExpenseById(@PathVariable Long id) {

        return service.getExpenseById(id);
    }

    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {

        return service.updateExpense(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {

        service.deleteExpense(id);
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
    public Page<ExpenseResponse> searchExpenses(

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

        return service.searchExpenses(
                category,
                minAmount,
                maxAmount,
                title,
                page,
                size,
                sortBy,
                direction
        );

    }
}
