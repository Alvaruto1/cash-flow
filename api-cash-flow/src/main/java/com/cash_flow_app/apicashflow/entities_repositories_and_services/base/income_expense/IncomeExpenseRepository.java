package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    Optional<IncomeExpense> findById(UUID incomeExpenseID);
    List<IncomeExpense> findByAccount_Id(UUID accountID);
}
