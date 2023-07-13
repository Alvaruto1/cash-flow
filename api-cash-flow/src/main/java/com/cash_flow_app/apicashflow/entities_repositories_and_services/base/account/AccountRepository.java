package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
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

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUsers_Username(String username);
    Optional<Account> findAccountById(UUID id);

}
