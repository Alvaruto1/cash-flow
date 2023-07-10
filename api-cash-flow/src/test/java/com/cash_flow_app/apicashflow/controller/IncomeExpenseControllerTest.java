package com.cash_flow_app.apicashflow.controller;

import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.dtos.UserDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class IncomeExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserDto userDtoTest, userDto2Test;
    AccountDto accountDtoTest, accountDto2Test;

    IncomeExpenseDto incomeExpenseDtoTest, incomeExpenseDto2Test;

    String token;

    @BeforeEach
    void setUp() throws Exception {
        ArrayList<HashMap<String, Object>> listHashes = new ArrayList<>();
        HashMap<String, Object> authorityHash = new HashMap<>();
        ArrayList<String> actions = new ArrayList<>();
        actions.add("CREATE");
        actions.add("READ");
        actions.add("DELETE");
        authorityHash.put("actions", actions);
        authorityHash.put("endpoint", "Account");

        HashMap<String, Object> authorityHash2 = new HashMap<>();
        ArrayList<String> actions2 = new ArrayList<>();
        actions2.add("CREATE");
        actions2.add("READ");
        authorityHash2.put("actions", actions2);
        authorityHash2.put("endpoint", "User");

        HashMap<String, Object> authorityHash3 = new HashMap<>();
        ArrayList<String> actions3 = new ArrayList<>();
        actions3.add("CREATE");
        actions3.add("READ");
        actions3.add("DELETE");
        authorityHash3.put("actions", actions3);
        authorityHash3.put("endpoint", "IncomeExpense");

        listHashes.add(authorityHash);
        listHashes.add(authorityHash2);
        listHashes.add(authorityHash3);
        userDtoTest = UserDto.builder()
                .username("usuario_test")
                .password("123456")
                .authorities(listHashes)
                .build();
        userDto2Test = UserDto.builder()
                .username("usuario_test_2")
                .password("123456")
                .authorities(listHashes)
                .build();

        accountDtoTest = AccountDto.builder()
                .description("descripcion de cuenta")
                .users(List.of("usuario_test"))
                .build();
        accountDto2Test = AccountDto.builder()
                .description("descripcion de cuenta2")
                .users(List.of("usuario_test"))
                .build();

        incomeExpenseDtoTest = IncomeExpenseDto.builder()
                .description("Ropa de navidad")
                .date(LocalDateTime.now())
                .category(IncomeExpense.Category.CLOTHING.name())
                .type(IncomeExpense.Type.EXPENSE.name())
                .paymentMethod(IncomeExpense.PaymentMethod.CASH.name())
                .value(BigDecimal.valueOf(10000))
                .build();

        incomeExpenseDto2Test = IncomeExpenseDto.builder()
                .description("Ropa de navidad 2")
                .date(LocalDateTime.now())
                .category(IncomeExpense.Category.CLOTHING.name())
                .type(IncomeExpense.Type.EXPENSE.name())
                .paymentMethod(IncomeExpense.PaymentMethod.CASH.name())
                .value(BigDecimal.valueOf(10000))
                .build();

        token = getToken(userDtoTest);

    }

    @Test
    void create() throws Exception {
        MvcResult result = createAccountResult(accountDtoTest);
        String json = result.getResponse().getContentAsString();
        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
        incomeExpenseDtoTest.setAccountId(UUID.fromString(accountDtoResponse.getId()).toString());
        //when
        mockMvc.perform(
                post("/api/v1/income_expense/create")
                        .header("Authorization", token)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomeExpenseDtoTest))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", CoreMatchers.is("Ropa de navidad")))
                .andReturn();
    }

    @Test
    void getIncomeExpense() throws Exception {
        //given
        MvcResult result = createIncomeExpenseResult(incomeExpenseDtoTest);
        String jsonIncomeExpense = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        IncomeExpenseDto incomeExpenseDtoResponse = mapper.readValue(jsonIncomeExpense, IncomeExpenseDto.class);

        //when
        mockMvc.perform(
                        get(String.format("/api/v1/income_expense/get/%s", incomeExpenseDtoResponse.getId()))
                                .header("Authorization", token)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(incomeExpenseDtoTest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", CoreMatchers.is("Ropa de navidad")))
                .andReturn();
    }

    @Test
    void getAllExpenses() throws Exception {
        //given
        MvcResult result = createIncomeExpenseResult(incomeExpenseDtoTest);
        MvcResult result2 = createIncomeExpenseResult(incomeExpenseDto2Test);

        String jsonIncomeExpense = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        IncomeExpenseDto incomeExpenseDtoResponse = mapper.readValue(jsonIncomeExpense, IncomeExpenseDto.class);
        //when
        mockMvc.perform(
                        get("/api/v1/income_expense/get_all/expenses/" + incomeExpenseDtoResponse.getAccountId())
                                .header("Authorization", token)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incomesExpensesDtos[0].description", CoreMatchers.is("Ropa de navidad")))
                //.andExpect(jsonPath("$.incomesExpensesDtos[1].description", CoreMatchers.is("Ropa de navidad 2")))
                .andReturn();
    }

    @Test
    void deleteAccount() throws Exception {
        //given
        MvcResult result = createIncomeExpenseResult(incomeExpenseDtoTest);
        String jsonIncomeExpense = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        IncomeExpenseDto incomeExpenseDtoResponse = mapper.readValue(jsonIncomeExpense, IncomeExpenseDto.class);
        //when
        mockMvc.perform(
                        delete("/api/v1/income_expense/delete/" + incomeExpenseDtoResponse.getId())
                                .header("Authorization", token)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        get(String.format("/api/v1/income_expense/get/%s", incomeExpenseDtoResponse.getId()))
                                .header("Authorization", token)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(incomeExpenseDtoTest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", CoreMatchers.is("Income Expense not found")))
                .andReturn();

    }

    MvcResult createAccountResult(AccountDto accountDto) throws Exception {
        return mockMvc.perform(
                post("/api/v1/account/create")
                        .header("Authorization", token)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
        ).andReturn();
    }

    MvcResult createIncomeExpenseResult(IncomeExpenseDto incomeExpenseDto) throws Exception {
        MvcResult resultAccount = createAccountResult(accountDtoTest);
        String json = resultAccount.getResponse().getContentAsString();
        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
        incomeExpenseDto.setAccountId(UUID.fromString(accountDtoResponse.getId()).toString());
        return mockMvc.perform(
                post("/api/v1/income_expense/create")
                        .header("Authorization", token)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomeExpenseDto))
        ).andReturn();
    }

    String getToken(UserDto userDto) throws Exception {
        mockMvc.perform(
                post("/api/v1/user/signup")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
        );
        MvcResult response = mockMvc.perform(
                post("/api/v1/user/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", userDto.getUsername(), userDto.getPassword()))
        ).andDo(MockMvcResultHandlers.print()).andReturn();
        return response.getResponse().getHeader("Authorization");
    }
}