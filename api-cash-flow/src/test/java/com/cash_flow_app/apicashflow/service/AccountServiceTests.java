//package com.cash_flow_app.apicashflow.service;
//
//import com.cash_flow_app.apicashflow.dtos.AccountDto;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.AuthorityService;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@Import({AccountService.class, UserService.class, AuthorityService.class})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class AccountServiceTests {
//
//    @Autowired
//    private AccountService accountService;
//
//    @Autowired
//    private UserService userService;
//
//    private User userTest;
//
//    private Account accountTest;
//    private AccountDto accountDtoTest;
//
//    @BeforeEach
//    void setup(){
//        //given
//        userTest = User.builder()
//                .username("usuario_prueba")
//                .password("123456")
//                .build();
//        accountDtoTest = AccountDto.builder()
//                .description("descripcion de cuenta")
//                .build();
//
//    }
//
//    @Test
//    void testCreateAccount(){
//        //given
//        User user = userService.save(userTest);
//        accountDtoTest.setUsers(List.of(user.getUsername()));
//        //when
//        Account account = accountService.save(accountDtoTest);
//        //then
//        assertThat(account).isNotNull();
//        assertThat(account.getDescription()).isEqualTo(accountDtoTest.getDescription());
//    }
//
//    @Test
//    void testDeleteAccount(){
//        //given
//        User user = userService.save(userTest);
//        accountDtoTest.setUsers(List.of(user.getUsername()));
//        //when
//        Account account = accountService.save(accountDtoTest);
//        accountService.delete(account);
//        //then
//        Optional<Account> deletedAccountOptional = accountService.getAccount(account.getId());
//        assertThat(deletedAccountOptional.isEmpty()).isEqualTo(true);
//    }
//
//    @Test
//    void testGetAccountsByUsername(){
//        //given
//        User user = userService.save(userTest);
//        accountDtoTest.setUsers(List.of(user.getUsername()));
//        Account savedAccount = accountService.save(accountDtoTest);
//        //when
//        List<Account> accounts = accountService.getAccountsByUsername(user.getUsername());
//        //then
//        assertThat(accounts).isNotNull();
//        assertThat(accounts.size()).isEqualTo(1);
//        assertTrue(accounts.contains(savedAccount));
//    }
//
//}
