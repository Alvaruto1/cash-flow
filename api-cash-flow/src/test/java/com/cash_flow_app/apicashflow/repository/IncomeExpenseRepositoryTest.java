package com.cash_flow_app.apicashflow.repository;

import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpenseRepository;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class IncomeExpenseRepositoryTest {

    @Autowired
    private IncomeExpenseRepository incomeExpenseRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    private IncomeExpenseDto incomeExpenseDtoTest;
    private IncomeExpense incomeExpenseTest;
    private Account accountTest;
    private User userTest;


    @BeforeEach
    void setup(){
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
        incomeExpenseTest = IncomeExpense.builder()
                .description("descritpion test")
                .type(IncomeExpense.Type.EXPENSE)
                .paymentMethod(IncomeExpense.PaymentMethod.CASH)
                .category(IncomeExpense.Category.FAMILY)
                .value(new BigDecimal(20000))
                .date(LocalDateTime.now())
                .account(accountTest)
                .build();
    }

    @Test
    void findById() {
        //given
        IncomeExpense savedIncomeExpense = incomeExpenseRepository.save(incomeExpenseTest);
        //when
        IncomeExpense foundIncomeExpense = incomeExpenseRepository.findById(savedIncomeExpense.getId()).get();
        //then
        assertEquals(foundIncomeExpense.getId(), savedIncomeExpense.getId());
    }

    @Test
    void findByAccount_Id() {
        //given
        IncomeExpense savedIncomeExpense = incomeExpenseRepository.save(incomeExpenseTest);
        //when
        List<IncomeExpense> foundIncomesExpenses = incomeExpenseRepository.findByAccount_Id(savedIncomeExpense.getAccount().getId());
        //then
        assertEquals(foundIncomesExpenses.size(), 1);
        assertEquals(foundIncomesExpenses.get(0).getId(), savedIncomeExpense.getId());
    }
}