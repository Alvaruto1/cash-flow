package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import com.cash_flow_app.apicashflow.dtos.ScheduledAccountDto;
import com.cash_flow_app.apicashflow.dtos.IncomesExpensesDtos;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduledAccountService {

    private final ScheduledAccountRepository scheduledAccountRepository;
    private final IncomeExpenseService incomeExpenseService;

    public ScheduledAccount save(@NonNull ScheduledAccountDto scheduledAccountDto) throws IOException {
        Optional<IncomeExpense> incomeExpense = incomeExpenseService.getIncomeExpense(scheduledAccountDto.getIncomeExpenseId());
        if(incomeExpense.isEmpty()){
            throw new IOException("Income-expense not found");
        }
        ScheduledAccount scheduledAccount = ScheduledAccount.builder()
                .periodicity(ScheduledAccount.Periodicity.valueOf(scheduledAccountDto.getPeriodicity()))
                .startDate(scheduledAccountDto.getStartDate())
                .endDate(scheduledAccountDto.getEndDate())
                .incomeExpense(incomeExpense.get())
                .build();
        return scheduledAccountRepository.save(scheduledAccount);
    }

    public void delete(@NonNull ScheduledAccount scheduledAccount) {
        scheduledAccountRepository.delete(scheduledAccount);
    }

    public Optional<ScheduledAccount> getScheduledAccount(@NonNull UUID uuid) {
        return scheduledAccountRepository.findById(uuid);
    }

    public Optional<ScheduledAccount> getScheduledAccountByIncomeExpenseId(@NonNull UUID incomeExpenseId) {
        return scheduledAccountRepository.findByIncomeExpense_Id(incomeExpenseId);
    }

    public ScheduledAccountDto scheduledAccountToDto(@NonNull ScheduledAccount scheduledAccount) {
        return ScheduledAccountDto.builder()
                .periodicity(scheduledAccount.getPeriodicity().name())
                .startDate(scheduledAccount.getStartDate())
                .endDate(scheduledAccount.getEndDate())
                .incomeExpenseId(scheduledAccount.getIncomeExpense().getId())
                .build();
    }

}
