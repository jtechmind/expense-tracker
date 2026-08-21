package com.jtech.expense_tracker.specification;

import com.jtech.expense_tracker.entity.Expense;
import com.jtech.expense_tracker.entity.ExpenseCategory;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ExpenseSpecification {

    public static Specification<Expense> hasCategory (
            ExpenseCategory category) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("category"),
                        category
                );
    }

    public static Specification<Expense> amountGreaterThanOrEqualTo(
            BigDecimal amount) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("amount"),
                        amount
                );

    }

    public static Specification<Expense> amountLessThanOrEqualTo(
            BigDecimal amount) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("amount"),
                        amount
                );

    }

    public static Specification<Expense> titleContains(
            String title) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("title")
                        ),
                        "%" + title.toLowerCase() +"%"
                );
    }


}
