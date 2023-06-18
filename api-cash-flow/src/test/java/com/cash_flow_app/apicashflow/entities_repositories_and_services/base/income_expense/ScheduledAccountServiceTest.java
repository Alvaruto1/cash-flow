package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense;

import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.dtos.ScheduledAccountDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduledAccountServiceTest {

    @Autowired
    private ScheduledAccountService scheduledAccountService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IncomeExpenseService incomeExpenseService;

    @Autowired
    private UserService userService;

    private ScheduledAccountDto scheduledAccountDto;

    @BeforeEach
    void setup() throws IOException {
        IncomeExpenseDto incomeExpenseDtoTest;
        Account accountTest;
        User userTest;
        userTest = User.builder()
                .username("usuario_prueba")
                .password("123456")
                .build();
        userService.save(userTest);
        AccountDto accountDto;
        accountDto = AccountDto.builder()
                .description("descripcion prueba")
                .build();
        accountDto.setUsers(List.of(userTest.getUsername()));
        accountTest = accountService.save(accountDto);
        incomeExpenseDtoTest = IncomeExpenseDto.builder()
                .accountId(accountTest.getId())
                .type(IncomeExpense.Type.EXPENSE.name())
                .paymentMethod(IncomeExpense.PaymentMethod.CASH.name())
                .category(IncomeExpense.Category.FAMILY.name())
                .value(new BigDecimal(20000))
                .date(LocalDateTime.now())
                .description("descripcion prueba")
                .build();
        IncomeExpense savedIncomeExpense = incomeExpenseService.save(incomeExpenseDtoTest);
        scheduledAccountDto = ScheduledAccountDto.builder()
                .incomeExpenseId(savedIncomeExpense.getId())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .periodicity(ScheduledAccount.Periodicity.SEMIANNUAL.name())
                .build();
    }

    @Test
    void save() throws IOException {
        //when
        ScheduledAccount savedScheduledAccount = scheduledAccountService.save(scheduledAccountDto);
        //then
        assertEquals(scheduledAccountDto.getStartDate(), savedScheduledAccount.getStartDate());
        assertEquals(scheduledAccountDto.getEndDate(), savedScheduledAccount.getEndDate());
        assertEquals(ScheduledAccount.Periodicity.valueOf(scheduledAccountDto.getPeriodicity()), savedScheduledAccount.getPeriodicity());
        assertEquals(scheduledAccountDto.getIncomeExpenseId(), savedScheduledAccount.getIncomeExpense().getId());
    }

    @Test
    void delete() throws IOException {
        //given
        ScheduledAccount savedScheduledAccount = scheduledAccountService.save(scheduledAccountDto);
        //when
        scheduledAccountService.delete(savedScheduledAccount);
        //then
        assertTrue(scheduledAccountService.getScheduledAccount(savedScheduledAccount.getId()).isEmpty());
    }

    @Test
    void getScheduledAccount() throws IOException {
        //given
        ScheduledAccount savedScheduledAccount = scheduledAccountService.save(scheduledAccountDto);
        //when
        Optional<ScheduledAccount> optionalScheduledAccounts = scheduledAccountService.getScheduledAccount(savedScheduledAccount.getId());
        ScheduledAccount scheduledAccount = optionalScheduledAccounts.get();
        //then
        assertEquals(scheduledAccount.getStartDate(), savedScheduledAccount.getStartDate());
        assertEquals(scheduledAccount.getEndDate(), savedScheduledAccount.getEndDate());
        assertEquals(scheduledAccount.getPeriodicity(), savedScheduledAccount.getPeriodicity());
        assertEquals(scheduledAccount.getIncomeExpense().getId(), savedScheduledAccount.getIncomeExpense().getId());
        assertEquals(scheduledAccount.getId(), savedScheduledAccount.getId());
    }

    @Test
    void getScheduledByIncomeExpenseId() throws IOException {
        //given
        ScheduledAccount savedScheduledAccount = scheduledAccountService.save(scheduledAccountDto);
        //when
        Optional<ScheduledAccount> optionalScheduledAccounts = scheduledAccountService.getScheduledAccountByIncomeExpenseId(savedScheduledAccount.getIncomeExpense().getId());
        ScheduledAccount scheduledAccount = optionalScheduledAccounts.get();
        //then
        assertEquals(scheduledAccount.getStartDate(), savedScheduledAccount.getStartDate());
        assertEquals(scheduledAccount.getEndDate(), savedScheduledAccount.getEndDate());
        assertEquals(scheduledAccount.getPeriodicity(), savedScheduledAccount.getPeriodicity());
        assertEquals(scheduledAccount.getIncomeExpense().getId(), savedScheduledAccount.getIncomeExpense().getId());
        assertEquals(scheduledAccount.getId(), savedScheduledAccount.getId());
    }
}