package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="incomes_expenses")
public class IncomeExpense {

    public IncomeExpense(String description, Category category, PaymentMethod paymentMethod, Type type, LocalDateTime date, Account account) {
        this.description = description;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.type = type;
        this.date = date;
        this.account = account;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime date;

    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(mappedBy = "incomeExpense", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ScheduledAccount scheduledAccount;

    enum Category {
        FOOD, HOUSING, TRANSPORT, HEALTH, EDUCATION, CLOTHING, TRAVEL, FAMILY, PETS, VEHICLES, SAVINGS
    }

    enum PaymentMethod {
        CASH, CREDIT_CARD, DEBIT_CARD, OTHERS
    }

    enum Type {
        INCOME, EXPENSE
    }
}
