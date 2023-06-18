package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="incomes_expenses")
public class IncomeExpense {

    public IncomeExpense(String description, Category category, PaymentMethod paymentMethod, Type type, LocalDateTime date, BigDecimal value, Account account) {
        this.description = description;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.type = type;
        this.date = date;
        this.account = account;
        this.value = value;
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

    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(mappedBy = "incomeExpense", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ScheduledAccount scheduledAccount;

    public enum Category {
        FOOD, HOUSING, TRANSPORT, HEALTH, EDUCATION, CLOTHING, TRAVEL, FAMILY, PETS, VEHICLES, SAVINGS
    }

    public enum PaymentMethod {
        CASH, CREDIT_CARD, DEBIT_CARD, OTHERS
    }

    public enum Type {
        INCOME, EXPENSE
    }
}
