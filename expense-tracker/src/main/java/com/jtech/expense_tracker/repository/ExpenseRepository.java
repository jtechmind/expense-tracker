package com.jtech.expense_tracker.repository;

import com.jtech.expense_tracker.entity.Expense;
import com.jtech.expense_tracker.entity.ExpenseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;


public interface ExpenseRepository
        extends JpaRepository<Expense, Long>,
        JpaSpecificationExecutor<Expense>
{
    Page<Expense> findByCategory(
            ExpenseCategory category,
            Pageable pageable
    );

    Page<Expense> findByAmountBetween(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable
    );

}
