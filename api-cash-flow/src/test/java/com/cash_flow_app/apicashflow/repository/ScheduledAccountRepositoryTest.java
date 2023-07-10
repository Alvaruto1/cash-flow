package com.cash_flow_app.apicashflow.repository;

import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ScheduledAccountRepositoryTest {

    @Autowired
    private ScheduledAccountRepository scheduledAccountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IncomeExpenseService incomeExpenseService;

    @Autowired
    private UserService userService;

    private ScheduledAccount scheduledAccount;

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
                .accountId(accountTest.getId().toString())
                .type(IncomeExpense.Type.EXPENSE.name())
                .paymentMethod(IncomeExpense.PaymentMethod.CASH.name())
                .category(IncomeExpense.Category.FAMILY.name())
                .value(new BigDecimal(20000))
                .date(LocalDateTime.now())
                .description("descripcion prueba")
                .build();
        IncomeExpense savedIncomeExpense = incomeExpenseService.save(incomeExpenseDtoTest);
        scheduledAccount = ScheduledAccount.builder()
                .incomeExpense(savedIncomeExpense)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .periodicity(ScheduledAccount.Periodicity.SEMIANNUAL)
                .build();
    }

    @Test
    void findById() {
        //given
        ScheduledAccount savedScheduledAccount = scheduledAccountRepository.save(scheduledAccount);
        //when
        ScheduledAccount foundScheduledAccount = scheduledAccountRepository.findById(savedScheduledAccount.getId()).get();
        //then
        assertEquals(foundScheduledAccount.getId(), savedScheduledAccount.getId());
    }

    @Test
    void findByIncomeExpense_Id() {
        //given
        ScheduledAccount savedScheduledAccount = scheduledAccountRepository.save(scheduledAccount);
        //when
        Optional<ScheduledAccount> optionalFoundScheduledAccount = scheduledAccountRepository.findByIncomeExpense_Id(savedScheduledAccount.getIncomeExpense().getId());
        ScheduledAccount foundScheduledAccount = optionalFoundScheduledAccount.get();
        //then
        assertEquals(foundScheduledAccount.getId(), savedScheduledAccount.getId());
    }
}