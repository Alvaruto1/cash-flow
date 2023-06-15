package com.cash_flow_app.apicashflow.repository;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User userTest;
    private List<User> usersTest;

    @BeforeEach
    void setup() {
        //given
        userTest = User.builder()
                .username("usuario_prueba")
                .password("123456")
                .build();

        usersTest = new ArrayList<>();
        for(int i=0; i<5; i++){
            usersTest.add(User.builder().username("user"+i).password("1234"+i).build());
        }
    }

    @Test
    void testSaveUser(){
        //when
        User savedUser = userRepository.save(userTest);
        //then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("usuario_prueba");
    }

    @Test
    void testGetUsers(){
        //given
        userRepository.saveAll(usersTest);
        //when
        List<User> savedUsers = userRepository.findAll();
        //then
        assertThat(savedUsers).isNotNull();
        assertThat(savedUsers.size()).isGreaterThan(usersTest.size());
    }

    @Test
    void testGetUser(){
        //given
        User savedUser = userRepository.save(userTest);
        //when
        User user = userRepository.getReferenceById(savedUser.getId());
        //then
        assertThat(user.getId()).isEqualTo(savedUser.getId());
        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
    }

    @Test
    void testUpdateUser(){
        //given
        userTest.setUsername("updated_username");
        //when
        User updatedUser = userRepository.save(userTest);
        //then
        assertThat(updatedUser.getUsername()).isEqualTo("updated_username");
    }

    @Test
    void testDeleteUser(){
        //given
        User savedUser = userRepository.save(userTest);
        //when
        userRepository.delete(savedUser);
        Optional<User> user = userRepository.findById(savedUser.getId());
        //then
        assertThat(user).isEmpty();
    }

}
