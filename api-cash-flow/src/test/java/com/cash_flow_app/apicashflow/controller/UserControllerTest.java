package com.cash_flow_app.apicashflow.controller;

import com.cash_flow_app.apicashflow.dtos.UserDto;
import com.cash_flow_app.apicashflow.dtos.UsersDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserDto userDtoTest, userDto2Test;

    @Autowired
    private UserService userService;
    @BeforeEach
    void setUp() {
        ArrayList<HashMap<String, Object>> listHashes = new ArrayList<>();
        HashMap<String, Object> authorityHash = new HashMap<>();
        ArrayList<String> actions = new ArrayList<>();
        actions.add("CREATE");
        actions.add("READ");
        authorityHash.put("actions", actions);
        authorityHash.put("endpoint", "Account");

        HashMap<String, Object> authorityHash2 = new HashMap<>();
        ArrayList<String> actions2 = new ArrayList<>();
        actions2.add("CREATE");
        actions2.add("READ");
        authorityHash2.put("actions", actions2);
        authorityHash2.put("endpoint", "User");
        listHashes.add(authorityHash);
        listHashes.add(authorityHash2);
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
    }

    @Test
    void signup() throws Exception {
        //when
        ResultActions response = mockMvc.perform(
                post("/api/v1/user/signup")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoTest))
        );
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", CoreMatchers.is("usuario_test")))
                .andExpect(jsonPath("$.authorities[0].actions[0]", CoreMatchers.is("CREATE")))
                .andExpect(jsonPath("$.authorities[0].endpoint", CoreMatchers.is("Account")));
    }

    @Test
    void getAll() throws Exception {
        //given
        mockMvc.perform(
                post("/api/v1/user/signup")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoTest))
        ).andDo(MockMvcResultHandlers.print()).andReturn();
        MvcResult response = mockMvc.perform(
                post("/api/v1/user/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"usuario_test\", \"password\": \"123456\" }")
        ).andDo(MockMvcResultHandlers.print()).andReturn();
        String token = response.getResponse().getHeader("Authorization");
        //when
        mockMvc.perform(
                get("/api/v1/user/get_all")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content("")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }
}