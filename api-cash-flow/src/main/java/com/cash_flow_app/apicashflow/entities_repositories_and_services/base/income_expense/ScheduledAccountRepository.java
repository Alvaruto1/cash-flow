package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScheduledAccountRepository extends JpaRepository<ScheduledAccount, Long> {
    Optional<ScheduledAccount> findById(UUID scheduledAccountID);
    Optional<ScheduledAccount> findByIncomeExpense_Id(UUID incomeExpenseID);
}
