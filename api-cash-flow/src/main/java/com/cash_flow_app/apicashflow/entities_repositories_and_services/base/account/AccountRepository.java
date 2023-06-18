package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUsers_Username(String username);
    Optional<Account> findAccountById(UUID id);


}
