package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    Optional<IncomeExpense> findById(UUID incomeExpenseID);
    List<IncomeExpense> findByAccount_Id(UUID accountID);
    List<IncomeExpense> findByAccount_IdAndType(UUID accountID, IncomeExpense.Type type);

    @Query("SELECT iE FROM incomes_expenses iE " +
            "WHERE iE.account.id = :accountId AND " +
            "iE.date BETWEEN :startDate and :endDate AND " +
            "iE.type = :type " +
            "ORDER BY iE.date DESC"
    )
    Page<IncomeExpense> findIncomesExpensesByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("accountId") UUID accountId, @Param("type") IncomeExpense.Type type, @NonNull Pageable pageable);
}
