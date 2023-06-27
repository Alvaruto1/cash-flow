package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.Authority;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="accounts")
public class Account {

    public Account(String description, ArrayList<User> users) {
        this.description = description;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    @ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
    @Builder.Default
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IncomeExpense> incomes_expenses;

    public void addUser(User user) {
        users.add(user);
        if(user.getAccounts() == null){
            user.setAccounts(new ArrayList<>());
        }
        user.getAccounts().add(this);
    }
}
