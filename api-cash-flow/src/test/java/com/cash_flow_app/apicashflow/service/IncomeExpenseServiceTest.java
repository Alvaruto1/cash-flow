package com.cash_flow_app.apicashflow.service;

import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpenseService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class IncomeExpenseServiceTest {

    @Autowired
    private IncomeExpenseService incomeExpenseService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    private IncomeExpenseDto incomeExpenseDtoTest;
    private Account accountTest;


    @BeforeEach
    void setup(){
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
    }

    @Test
    void save() throws IOException {
        //when
        IncomeExpense incomeExpense = incomeExpenseService.save(incomeExpenseDtoTest);
        //then
        assertEquals(incomeExpense.getDescription(), incomeExpenseDtoTest.getDescription());
        assertEquals(incomeExpense.getType().name(), incomeExpenseDtoTest.getType());
    }

    @Test
    void delete() throws IOException {
        //given
        IncomeExpense incomeExpense = incomeExpenseService.save(incomeExpenseDtoTest);
        //when
        incomeExpenseService.delete(incomeExpense);
        //then
        assertTrue(incomeExpenseService.getIncomeExpense(incomeExpense.getId()).isEmpty());
    }

    @Test
    void getIncomeExpense() throws IOException {
        //given
        IncomeExpense incomeExpense = incomeExpenseService.save(incomeExpenseDtoTest);
        //when
        IncomeExpense foundIncomeExpense = incomeExpenseService.getIncomeExpense(incomeExpense.getId()).get();
        //then
        assertEquals(foundIncomeExpense.getDescription(), incomeExpense.getDescription());
        assertEquals(foundIncomeExpense.getType().name(), incomeExpense.getType().name());
        assertEquals(foundIncomeExpense.getPaymentMethod().name(), incomeExpense.getPaymentMethod().name());
        assertEquals(foundIncomeExpense.getCategory().name(), incomeExpense.getCategory().name());
        assertEquals(foundIncomeExpense.getValue(), incomeExpense.getValue());
        assertEquals(foundIncomeExpense.getDate(), incomeExpense.getDate());
    }

    @Test
    void getIncomesExpensesByAccountId() throws IOException {
        //given
        IncomeExpense incomeExpense = incomeExpenseService.save(incomeExpenseDtoTest);
        IncomeExpenseDto incomeExpenseDto = IncomeExpenseDto.builder()
                .accountId(accountTest.getId())
                .type(IncomeExpense.Type.INCOME.name())
                .paymentMethod(IncomeExpense.PaymentMethod.CREDIT_CARD.name())
                .category(IncomeExpense.Category.FOOD.name())
                .value(new BigDecimal(10000))
                .date(LocalDateTime.now())
                .description("descripcion prueba 2")
                .build();
        IncomeExpense incomeExpense2 = incomeExpenseService.save(incomeExpenseDto);
        //when
        List<IncomeExpense> foundIncomesExpenses = incomeExpenseService.getIncomesExpensesByAccountId(accountTest.getId());
        //then
        assertEquals(foundIncomesExpenses.size(), 2);
        assertTrue(foundIncomesExpenses.contains(incomeExpense));
        assertTrue(foundIncomesExpenses.contains(incomeExpense2));
    }
}