package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="scheduled_accounts")
public class ScheduledAccount {
    public ScheduledAccount(Periodicity periodicity, LocalDateTime startDate, LocalDateTime endDate) {
        this.periodicity = periodicity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Periodicity periodicity;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @JoinColumn(name = "income_expense_id")
    @OneToOne(fetch = FetchType.LAZY)
    private IncomeExpense incomeExpense;

    public enum Periodicity {
        DAILY, WEEKLY, MONTHLY, BIMONTHLY, SEMIANNUAL, ANNUAL
    }

}
