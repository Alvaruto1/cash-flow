//package com.cash_flow_app.apicashflow.repository;
//
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
//import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class AccountRepositoryTest {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    private Account accountTest;
//    private List<Account> accountsTest;
//
//    @BeforeEach
//    void setup() {
//        //given
//        accountTest = Account.builder()
//                .description("descripcion prueba")
//                .build();
//
//        accountsTest = new ArrayList<>();
//        for(int i=0; i<5; i++){
//            accountsTest.add(Account.builder().description("descripcion"+i).build());
//        }
//    }
//
//    @Test
//    void testSaveAccount(){
//        //when
//        Account savedAccount = accountRepository.save(accountTest);
//        //then
//        assertThat(savedAccount).isNotNull();
//        assertThat(savedAccount.getDescription()).isEqualTo("descripcion prueba");
//    }
//
//    @Test
//    void testGetAccounts(){
//        //given
//        accountRepository.saveAll(accountsTest);
//        //when
//        List<Account> savedAccounts = accountRepository.findAll();
//        //then
//        assertThat(savedAccounts).isNotNull();
//        assertThat(savedAccounts.size()).isGreaterThanOrEqualTo(accountsTest.size());
//    }
//
//    @Test
//    void testGetAccount(){
//        //given
//        Account savedAccount = accountRepository.save(accountTest);
//        //when
//        Account account = accountRepository.getReferenceById(savedAccount.getId());
//        //then
//        assertThat(account.getId()).isEqualTo(savedAccount.getId());
//        assertThat(account.getDescription()).isEqualTo(savedAccount.getDescription());
//    }
//
//    @Test
//    void testUpdateAccount(){
//        //given
//        accountTest.setDescription("updated_description");
//        //when
//        Account updatedAccount = accountRepository.save(accountTest);
//        //then
//        assertThat(updatedAccount.getDescription()).isEqualTo("updated_description");
//    }
//
//    @Test
//    void testDeleteAccount(){
//        //given
//        Account savedAccount = accountRepository.save(accountTest);
//        //when
//        accountRepository.delete(savedAccount);
//        Optional<Account> account = accountRepository.findById(savedAccount.getId());
//        //then
//        assertThat(account).isEmpty();
//    }
//
//}
