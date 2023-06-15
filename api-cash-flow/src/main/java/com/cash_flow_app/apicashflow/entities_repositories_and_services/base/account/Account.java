package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.Authority;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="accounts")
public class Account {

    public Account(String description, List<User> users) {
        this.description = description;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    @ManyToMany(mappedBy = "accounts")
    private List<User> users;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomeExpense> incomes_expenses;
}
