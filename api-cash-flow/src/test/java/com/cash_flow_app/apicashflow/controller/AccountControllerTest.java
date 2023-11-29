//package com.cash_flow_app.apicashflow.controller;
//
//import com.cash_flow_app.apicashflow.dtos.AccountDto;
//import com.cash_flow_app.apicashflow.dtos.UserDto;
//import com.cash_flow_app.apicashflow.dtos.UsersDto;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
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
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class AccountControllerTest {
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
//        listHashes.add(authorityHash);
//        listHashes.add(authorityHash2);
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
//        token = getToken(userDtoTest);
//    }
//
//    @Test
//    void create() throws Exception {
//        //when
//        mockMvc.perform(
//                post("/api/v1/account/create")
//                        .header("Authorization", token)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountDtoTest))
//        )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.description", CoreMatchers.is("descripcion de cuenta")))
//                .andReturn();
//    }
//
//    @Test
//    void getAccount() throws Exception {
//        //given
//        MvcResult result = createAccountResult(accountDtoTest);
//
//        String json = result.getResponse().getContentAsString();
//        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
//
//        //when
//        mockMvc.perform(
//                        get(String.format("/api/v1/account/get/%s", accountDtoResponse.getId()))
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(accountDtoTest))
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.description", CoreMatchers.is("descripcion de cuenta")))
//                .andReturn();
//    }
//
//    @Test
//    void getAllByUsername() throws Exception {
//        //given
//        MvcResult result = createAccountResult(accountDtoTest);
//        MvcResult result2 = createAccountResult(accountDto2Test);
//        //when
//        mockMvc.perform(
//                        get(String.format("/api/v1/account/get_all/by_username/%s", userDtoTest.getUsername()))
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountDtos[0].description", CoreMatchers.is("descripcion de cuenta")))
//                .andExpect(jsonPath("$.accountDtos[1].description", CoreMatchers.is("descripcion de cuenta2")))
//                .andReturn();
//    }
//
//    @Test
//    void getAllByCurrentUser() throws Exception {
//        //given
//        MvcResult result = createAccountResult(accountDtoTest);
//        MvcResult result2 = createAccountResult(accountDto2Test);
//        //when
//        mockMvc.perform(
//                        get("/api/v1/account/get_all/by_current_user")
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountDtos[0].description", CoreMatchers.is("descripcion de cuenta")))
//                .andExpect(jsonPath("$.accountDtos[1].description", CoreMatchers.is("descripcion de cuenta2")))
//                .andReturn();
//    }
//
//    @Test
//    void deleteAccount() throws Exception {
//        //given
//        MvcResult result = createAccountResult(accountDtoTest);
//        String json = result.getResponse().getContentAsString();
//        AccountDto accountDtoResponse = new ObjectMapper().readValue(json, AccountDto.class);
//        //when
//        mockMvc.perform(
//                        delete("/api/v1/account/delete/" + accountDtoResponse.getId())
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        mockMvc.perform(
//                        get(String.format("/api/v1/account/get/%s", accountDtoResponse.getId()))
//                                .header("Authorization", token)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(accountDtoTest))
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().is(400))
//                .andExpect(jsonPath("$.error", CoreMatchers.is("Account not found")))
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
//}