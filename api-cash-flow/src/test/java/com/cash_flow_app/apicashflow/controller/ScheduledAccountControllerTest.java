//package com.cash_flow_app.apicashflow.controller;
//
//import com.cash_flow_app.apicashflow.dtos.AccountDto;
//import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
//import com.cash_flow_app.apicashflow.dtos.ScheduledAccountDto;
//import com.cash_flow_app.apicashflow.dtos.UserDto;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.ScheduledAccount;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.hamcrest.CoreMatchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.UUID;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class ScheduledAccountControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    UserDto userDtoTest, userDto2Test;
//    AccountDto accountDtoTest, accountDto2Test;
//
//    IncomeExpenseDto incomeExpenseDtoTest, incomeExpenseDto2Test;
//
//    ScheduledAccountDto scheduledAccountDtoTest, scheduledAccountDto2Test;
//
//    String token;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        ArrayList<HashMap<String, Object>> listHashes = new ArrayList<>();
//        HashMap<String, Object> authorityHash = new HashMap<>();
//        ArrayList<String> actions = new ArrayList<>();
//        actions.add("CREATE");
//        actions.add("READ");
//        actions.add("DELETE");
//        authorityHash.put("actions", actions);
//        authorityHash.put("endpoint", "Account");
//
//        HashMap<String, Object> authorityHash2 = new HashMap<>();
//        ArrayList<String> actions2 = new ArrayList<>();
//        actions2.add("CREATE");
//        actions2.add("READ");
//        authorityHash2.put("actions", actions2);
//        authorityHash2.put("endpoint", "User");
//
//        HashMap<String, Object> authorityHash3 = new HashMap<>();
//        ArrayList<String> actions3 = new ArrayList<>();
//        actions3.add("CREATE");
//        actions3.add("READ");
//        actions3.add("DELETE");
//        authorityHash3.put("actions", actions3);
//        authorityHash3.put("endpoint", "IncomeExpense");
//
//        HashMap<String, Object> authorityHash4 = new HashMap<>();
//        ArrayList<String> actions4 = new ArrayList<>();
//        actions4.add("CREATE");
//        actions4.add("READ");
//        actions4.add("DELETE");
//        authorityHash4.put("actions", actions4);
//        authorityHash4.put("endpoint", "ScheduledAccount");
//
//        listHashes.add(authorityHash);
//        listHashes.add(authorityHash2);
//        listHashes.add(authorityHash3);
//        listHashes.add(authorityHash4);
//        userDtoTest = UserDto.builder()
//                .username("usuario_test")
//                .password("123456")
//                .authorities(listHashes)
//                .build();
//        userDto2Test = UserDto.builder()
//                .username("usuario_test_2")
//                .password("123456")
//                .authorities(listHashes)
//                .build();
//
//        accountDtoTest = AccountDto.builder()
//                .description("descripcion de cuenta")
//                .users(List.of("usuario_test"))
//                .build();
//        accountDto2Test = AccountDto.builder()
//                .description("descripcion de cuenta2")
//                .users(List.of("usuario_test"))
//                .build();
//
//        incomeExpenseDtoTest = IncomeExpenseDto.builder()
//                .description("Ropa de navidad")
//                .date(LocalDateTime.now())
//                .category(IncomeExpense.Category.CLOTHING.name())
//                .type(IncomeExpense.Type.EXPENSE.name())
//                .paymentMethod(IncomeExpense.PaymentMethod.CASH.name())
//                .value(BigDecimal.valueOf(10000))
//                .build();
//
//        incomeExpenseDto2Test = IncomeExpenseDto.builder()
//                .description("Ropa de navidad 2")
//                .date(LocalDateTime.now())
//                .category(IncomeExpense.Category.CLOTHING.name())
//                .type(IncomeExpense.Type.EXPENSE.name())
//                .paymentMethod(IncomeExpense.PaymentMethod.CASH.name())
//                .value(BigDecimal.valueOf(10000))
//                .build();
//
//        LocalDateTime localDateTime = LocalDateTime.now();
//        localDateTime = localDateTime.plus(Duration.ofDays(360));
//
//        scheduledAccountDtoTest = ScheduledAccountDto.builder()
//                .periodicity("MONTHLY")
//                .startDate(LocalDateTime.now())
//                .endDate(localDateTime)
//                .build();
//
//        localDateTime = localDateTime.plus(Duration.ofDays(180));
//        scheduledAccountDto2Test = ScheduledAccountDto.builder()
//                .periodicity("BIMONTHLY")
//                .startDate(LocalDateTime.now())
//                .endDate(localDateTime)
//                .build();
//
//        token = getToken(userDtoTest);
//
//    }
//
//    @Test
//    void create() throws Exception {
//        MvcResult result = createAccountResult(accountDtoTest);
//        String json = result.getResponse().getContentAsString();
//        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
//        incomeExpenseDtoTest.setAccountId(UUID.fromString(accountDtoResponse.getId()).toString());
//
//        MvcResult result2 = createIncomeExpenseResult(incomeExpenseDtoTest, accountDtoResponse.getId());
//        String json2 = result2.getResponse().getContentAsString();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        IncomeExpenseDto incomeExpenseDtoResponse = mapper.readValue(json2, IncomeExpenseDto.class);
//        scheduledAccountDtoTest.setIncomeExpenseId(UUID.fromString(incomeExpenseDtoResponse.getId()));
//        //when
//        mockMvc.perform(
//                post("/api/v1/scheduled_account/create")
//                        .header("Authorization", token)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(scheduledAccountDtoTest))
//        )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.periodicity", CoreMatchers.is("MONTHLY")))
//                .andReturn();
//    }
//
//    @Test
//    void getScheduledAccount() throws Exception {
//        //given
//        MvcResult resultAccount = createAccountResult(accountDtoTest);
//        String json = resultAccount.getResponse().getContentAsString();
//        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
//
//        MvcResult resultIncomeExpense = createIncomeExpenseResult(incomeExpenseDtoTest, accountDtoResponse.getId());
//        String jsonIncomeExpense = resultIncomeExpense.getResponse().getContentAsString();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        IncomeExpenseDto incomeExpenseDtoResponse = mapper.readValue(jsonIncomeExpense, IncomeExpenseDto.class);
//
//        MvcResult result = createScheduledAccountResult(scheduledAccountDtoTest, incomeExpenseDtoResponse.getId());
//        String jsonScheduledAccount = result.getResponse().getContentAsString();
//        mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        ScheduledAccountDto scheduledAccountDtoResponse = mapper.readValue(jsonScheduledAccount, ScheduledAccountDto.class);
//        scheduledAccountDtoResponse.setId(UUID.fromString(scheduledAccountDtoResponse.getId()).toString());
//        //when
//        mockMvc.perform(
//                        get(String.format("/api/v1/scheduled_account/get/%s", scheduledAccountDtoResponse.getId()))
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.periodicity", CoreMatchers.is("MONTHLY")))
//                .andReturn();
//    }
//
//    @Test
//    void getScheduledAccountByIncomeExpenseId() throws Exception {
//        //given
//        MvcResult resultAccount = createAccountResult(accountDtoTest);
//        String json = resultAccount.getResponse().getContentAsString();
//        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
//
//        MvcResult resultIncomeExpense = createIncomeExpenseResult(incomeExpenseDtoTest, accountDtoResponse.getId());
//        String jsonIncomeExpense = resultIncomeExpense.getResponse().getContentAsString();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        IncomeExpenseDto incomeExpenseDtoResponse = mapper.readValue(jsonIncomeExpense, IncomeExpenseDto.class);
//
//        MvcResult result = createScheduledAccountResult(scheduledAccountDtoTest, incomeExpenseDtoResponse.getId());
//        String jsonScheduledAccount = result.getResponse().getContentAsString();
//        mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        ScheduledAccountDto scheduledAccountDtoResponse = mapper.readValue(jsonScheduledAccount, ScheduledAccountDto.class);
//        scheduledAccountDtoResponse.setId(UUID.fromString(scheduledAccountDtoResponse.getId()).toString());
//        //when
//        mockMvc.perform(
//                        get(String.format("/api/v1/scheduled_account/get/by_income_expense/%s", incomeExpenseDtoResponse.getId()))
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.periodicity", CoreMatchers.is("MONTHLY")))
//                .andReturn();
//    }
//
//    @Test
//    void deleteScheduledAccount() throws Exception {
//        //given
//        MvcResult resultAccount = createAccountResult(accountDtoTest);
//        String json = resultAccount.getResponse().getContentAsString();
//        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
//
//        MvcResult resultIncomeExpense = createIncomeExpenseResult(incomeExpenseDtoTest, accountDtoResponse.getId());
//        String jsonIncomeExpense = resultIncomeExpense.getResponse().getContentAsString();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        IncomeExpenseDto incomeExpenseDtoResponse = mapper.readValue(jsonIncomeExpense, IncomeExpenseDto.class);
//
//        MvcResult result = createScheduledAccountResult(scheduledAccountDtoTest, incomeExpenseDtoResponse.getId());
//        String jsonScheduledAccount = result.getResponse().getContentAsString();
//        mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        ScheduledAccountDto scheduledAccountDtoResponse = mapper.readValue(jsonScheduledAccount, ScheduledAccountDto.class);
//        //when
//        mockMvc.perform(
//                        delete("/api/v1/scheduled_account/delete/" + scheduledAccountDtoResponse.getId())
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        mockMvc.perform(
//                        get(String.format("/api/v1/scheduled_account/get/%s", scheduledAccountDtoResponse.getId()))
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().is(400))
//                .andExpect(jsonPath("$.error", CoreMatchers.is("Scheduled Account not found")))
//                .andReturn();
//
//    }
//
//    MvcResult createAccountResult(AccountDto accountDto) throws Exception {
//        return mockMvc.perform(
//                post("/api/v1/account/create")
//                        .header("Authorization", token)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountDto))
//        ).andReturn();
//    }
//
//    MvcResult createIncomeExpenseResult(IncomeExpenseDto incomeExpenseDto, String accountId) throws Exception {
//        incomeExpenseDto.setAccountId(accountId);
//        return mockMvc.perform(
//                post("/api/v1/income_expense/create")
//                        .header("Authorization", token)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(incomeExpenseDto))
//        ).andReturn();
//    }
//
//    MvcResult createScheduledAccountResult(ScheduledAccountDto scheduledAccountDto, String incomeExpenseId) throws Exception {
//        scheduledAccountDto.setIncomeExpenseId(UUID.fromString(incomeExpenseId));
//        return mockMvc.perform(
//                post("/api/v1/scheduled_account/create")
//                        .header("Authorization", token)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(scheduledAccountDto))
//        ).andReturn();
//    }
//
//    String getToken(UserDto userDto) throws Exception {
//        mockMvc.perform(
//                post("/api/v1/user/signup")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto))
//        );
//        MvcResult response = mockMvc.perform(
//                post("/api/v1/user/login")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", userDto.getUsername(), userDto.getPassword()))
//        ).andDo(MockMvcResultHandlers.print()).andReturn();
//        return response.getResponse().getHeader("Authorization");
//    }
//
//
//}