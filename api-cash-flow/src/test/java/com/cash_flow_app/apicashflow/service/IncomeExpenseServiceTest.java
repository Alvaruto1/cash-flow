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
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .accountId(accountTest.getId().toString())
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
                .accountId(accountTest.getId().toString())
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

    @Test
    void getExpensesOrIncomesByAccountId() throws IOException {
        //given
        for(int i=0; i<5; i++){
            IncomeExpenseDto expenseDto = IncomeExpenseDto.builder()
                    .accountId(accountTest.getId().toString())
                    .type(IncomeExpense.Type.EXPENSE.name())
                    .paymentMethod(IncomeExpense.PaymentMethod.CREDIT_CARD.name())
                    .category(IncomeExpense.Category.FOOD.name())
                    .value(new BigDecimal(10000))
                    .date(LocalDateTime.now())
                    .description("descripcion prueba 2 gasto")
                    .build();
            IncomeExpenseDto incomeDto = IncomeExpenseDto.builder()
                    .accountId(accountTest.getId().toString())
                    .type(IncomeExpense.Type.INCOME.name())
                    .paymentMethod(IncomeExpense.PaymentMethod.CREDIT_CARD.name())
                    .category(IncomeExpense.Category.HOUSING.name())
                    .value(new BigDecimal(15000))
                    .date(LocalDateTime.now())
                    .description("descripcion prueba 2 ingreso")
                    .build();
            IncomeExpense income = incomeExpenseService.save(incomeDto);
            IncomeExpense expense = incomeExpenseService.save(expenseDto);
        }
        //when
        List<IncomeExpense> foundExpenses = incomeExpenseService.getExpensesByAccountId(accountTest.getId());
        List<IncomeExpense> foundIncomes = incomeExpenseService.getIncomesByAccountId(accountTest.getId());
        //then
        assertEquals(foundExpenses.size(), 5);
        assertEquals(foundIncomes.size(), 5);
        assertEquals(foundExpenses.get(0).getType(), IncomeExpense.Type.EXPENSE);
        assertEquals(foundIncomes.get(0).getType(), IncomeExpense.Type.INCOME);
    }

    @Test
    void getIncomesByAccountIdAndDate() throws IOException {
        createIncomesExpenses(10);
        List<IncomeExpense> pageIcomesExpensesList = incomeExpenseService.getIncomesByAccountIdAndDate(accountTest.getId(), LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        for (IncomeExpense incomeExpense: pageIcomesExpensesList){
            assertTrue(incomeExpense.getDate().isBefore(LocalDateTime.now().plusDays(30)) && incomeExpense.getDate().isAfter(LocalDateTime.now()));
        }
    }

    @Test
    void getExpensesByAccountIdAndDate() throws IOException {
        createIncomesExpenses(10);
        List<IncomeExpense> pageIcomesExpensesList = incomeExpenseService.getExpensesByAccountIdAndDate(accountTest.getId(), LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        for (IncomeExpense incomeExpense: pageIcomesExpensesList){
            assertTrue(incomeExpense.getDate().isBefore(LocalDateTime.now().plusDays(30)) && incomeExpense.getDate().isAfter(LocalDateTime.now()));
        }
    }

    @Test
    void getTotalIncomesExpenses() throws IOException {
        createIncomesExpenses(10);
        List<IncomeExpense> pageIcomesExpensesList = incomeExpenseService.getIncomesByAccountIdAndDate(accountTest.getId(), LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        BigDecimal totalIncomesExpenses = pageIcomesExpensesList.stream().map(IncomeExpense::getValue).reduce(BigDecimal::add).get();
        BigDecimal totalValues = incomeExpenseService.getTotalIncomesExpenses(pageIcomesExpensesList);
        assertEquals(totalIncomesExpenses, totalValues);
    }

    public static Pageable defaultPage(@NonNull Integer pageNumber){
        return PageRequest.of(pageNumber, 20);
    }

    void createIncomesExpenses(int quantity) throws IOException {
        for(int i=0; i<quantity; i++){
            int randomInt = (int) (Math.random() * 2);
            int randomInt2 = (int) (Math.random() * 60);
            IncomeExpenseDto incomeExpenseDto = IncomeExpenseDto.builder()
                    .accountId(accountTest.getId().toString())
                    .type(randomInt == 0 ? IncomeExpense.Type.INCOME.name() : IncomeExpense.Type.EXPENSE.name())
                    .paymentMethod(IncomeExpense.PaymentMethod.CREDIT_CARD.name())
                    .category(IncomeExpense.Category.FOOD.name())
                    .value(new BigDecimal(10000))
                    .date(LocalDateTime.now().plusDays(randomInt2))
                    .description("descripcion prueba 2 gasto")
                    .build();
            IncomeExpense incomeExpense = incomeExpenseService.save(incomeExpenseDto);
        }
    }


}