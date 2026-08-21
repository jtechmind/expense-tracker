package com.jtech.expense_tracker.service;

import com.jtech.expense_tracker.dto.ExpenseRequest;
import com.jtech.expense_tracker.dto.ExpenseResponse;
import com.jtech.expense_tracker.entity.Expense;
import com.jtech.expense_tracker.entity.ExpenseCategory;
import com.jtech.expense_tracker.exception.ExpenseNotFoundException;
import com.jtech.expense_tracker.repository.ExpenseRepository;
import com.jtech.expense_tracker.specification.ExpenseSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public Page<ExpenseResponse> getAllExpenses(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public ExpenseResponse createExpense(ExpenseRequest request) {

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());

        expense.setCreatedAt(LocalDateTime.now());

        Expense savedExpense = repository.save(expense);

        return convertToResponse(savedExpense);
    }

    private ExpenseResponse convertToResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setExpenseDate(expense.getExpenseDate());
        response.setCreatedAt(expense.getCreatedAt());
        response.setUpdatedAt(expense.getUpdatedAt());
        return response;
    }

    public ExpenseResponse getExpenseById(Long id) {
        Expense expense =  repository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id:" +id));

        return convertToResponse(expense);
    }

    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {

        Expense existingExpense = repository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " +id));

        existingExpense.setTitle(request.getTitle());
        existingExpense.setDescription(request.getDescription());
        existingExpense.setAmount(request.getAmount());
        existingExpense.setCategory(request.getCategory());
        existingExpense.setExpenseDate(request.getExpenseDate());

        existingExpense.setUpdatedAt(LocalDateTime.now());

        Expense savedExpense =  repository.save(existingExpense);

        return convertToResponse(savedExpense);
    }

    public void deleteExpense(Long id) {

        if(!repository.existsById(id)) {
            throw new ExpenseNotFoundException("Expense not found with id: " + id);
        }

        repository.deleteById(id);
    }

public Page<ExpenseResponse> getExpensesByCategory(
        ExpenseCategory category,
        int page,
        int size,
        String sortBy,
        String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        return repository.findByCategory(category,pageable)
                .map(this::convertToResponse);
}

public Page<ExpenseResponse> getExpensesByAmountRange(
        BigDecimal minimumAmount,
        BigDecimal maximumAmount,
        int page,
        int size,
        String sortBy,
        String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        return repository
                .findByAmountBetween(minimumAmount, maximumAmount, pageable)
                .map(this::convertToResponse);
}

public Page<ExpenseResponse> searchExpenses(
        ExpenseCategory category,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        String title,
        int page,
        int size,
        String sortBy,
        String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

    Specification<Expense> specification =
            (root, query, criteriaBuilder) ->
                    criteriaBuilder.conjunction();

    if(category != null) {

        specification = specification.and(
                ExpenseSpecification.hasCategory(category)
        );
    }

    if(minAmount != null) {

        specification = specification.and(
                ExpenseSpecification.amountGreaterThanOrEqualTo(minAmount)
        );
    }

    if(maxAmount != null) {

        specification = specification.and(
                ExpenseSpecification.amountLessThanOrEqualTo(maxAmount)
        );
    }

    if(title != null && !title.isBlank()) {

        specification = specification.and(
                ExpenseSpecification.titleContains(title)
        );
    }

    return repository
            .findAll(specification, pageable)
            .map(this::convertToResponse);

}

}
