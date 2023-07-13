package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.dtos.IncomesExpensesDtos;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncomeExpenseService {

    private final IncomeExpenseRepository incomeExpenseRepository;
    private final AccountService accountService;

    public IncomeExpense save(@NonNull IncomeExpenseDto incomeExpenseDto) throws IOException {
        Optional<Account> account = accountService.getAccount(UUID.fromString(incomeExpenseDto.getAccountId()));
        if(account.isEmpty()){
            throw new IOException("Account not found");
        }
        IncomeExpense incomeExpense = IncomeExpense.builder()
                .date(incomeExpenseDto.getDate())
                .value(incomeExpenseDto.getValue())
                .paymentMethod(IncomeExpense.PaymentMethod.valueOf(incomeExpenseDto.getPaymentMethod()))
                .category(IncomeExpense.Category.valueOf(incomeExpenseDto.getCategory()))
                .type(IncomeExpense.Type.valueOf(incomeExpenseDto.getType()))
                .description(incomeExpenseDto.getDescription())
                .account(account.get())
                .build();
        return incomeExpenseRepository.save(incomeExpense);
    }

    public void delete(@NonNull IncomeExpense incomeExpense) {
        incomeExpenseRepository.delete(incomeExpense);
    }

    public Optional<IncomeExpense> getIncomeExpense(@NonNull UUID uuid) {
        return incomeExpenseRepository.findById(uuid);
    }

    public List<IncomeExpense> getIncomesExpensesByAccountId(@NonNull UUID accountId) {
        return incomeExpenseRepository.findByAccount_Id(accountId);
    }

    public List<IncomeExpense> getExpensesByAccountId(@NonNull UUID accountId) {
        return incomeExpenseRepository.findByAccount_IdAndType(accountId, IncomeExpense.Type.EXPENSE);
    }

    public List<IncomeExpense> getIncomesByAccountId(@NonNull UUID accountId) {
        return incomeExpenseRepository.findByAccount_IdAndType(accountId, IncomeExpense.Type.INCOME);
    }

    public Page<IncomeExpense> getIncomesByAccountIdAndDate(@NonNull UUID accountId, @NonNull Pageable pageable, @NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate) {
        return incomeExpenseRepository.findIncomesExpensesByDate(startDate, endDate, accountId, IncomeExpense.Type.INCOME, pageable);
    }

    public Page<IncomeExpense> getExpensesByAccountIdAndDate(@NonNull UUID accountId, @NonNull Pageable pageable, @NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate) {
        return incomeExpenseRepository.findIncomesExpensesByDate(startDate, endDate, accountId, IncomeExpense.Type.EXPENSE, pageable);
    }

    public IncomeExpenseDto incomeExpenseToDto(@NonNull IncomeExpense incomeExpense) {
        return IncomeExpenseDto.builder()
                .id(incomeExpense.getId().toString())
                .description(incomeExpense.getDescription())
                .category(incomeExpense.getCategory().name())
                .paymentMethod(incomeExpense.getPaymentMethod().name())
                .type(incomeExpense.getType().name())
                .date(incomeExpense.getDate())
                .value(incomeExpense.getValue())
                .accountId(incomeExpense.getAccount().getId().toString())
                .build();
    }

    public BigDecimal getTotalIncomesExpenses(List<IncomeExpense> incomesExpenses) {
        BigDecimal total = BigDecimal.ZERO;
        for (IncomeExpense incomeExpense : incomesExpenses) {
            total = total.add(incomeExpense.getValue());
        }
        return total;
    }

    public IncomesExpensesDtos incomesExpensesToDtos(@NonNull List<IncomeExpense> incomesExpenses) {
        IncomesExpensesDtos incomesExpensesDtos = IncomesExpensesDtos.builder().build();
        BigDecimal total = getTotalIncomesExpenses(incomesExpenses);
        for (IncomeExpense incomeExpense : incomesExpenses) {
            incomesExpensesDtos.getIncomesExpensesDtos().add(incomeExpenseToDto(incomeExpense));
        }
        incomesExpensesDtos.setTotal(total);
        return incomesExpensesDtos;
    }

}
