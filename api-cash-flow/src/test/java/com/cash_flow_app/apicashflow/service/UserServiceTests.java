package com.cash_flow_app.apicashflow.service;
import com.cash_flow_app.apicashflow.dtos.UserDto;
import com.cash_flow_app.apicashflow.dtos.UsersDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.AuthorityService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

@DataJpaTest
@Import({AuthorityService.class, UserService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTests {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserService userService;

    private User userTest;
    private UserDto userDtoTest;
    private List<User> usersTest;

    @BeforeEach
    void setup(){
        //given
        userTest = User.builder()
                .username("usuario_prueba")
                .password("123456")
                .build();
        ArrayList<HashMap<String, Object>> listHashes = new ArrayList<>();
        HashMap<String, Object> authorityHash = new HashMap<>();
        ArrayList<String> actions = new ArrayList<>();
        actions.add("CREATE");
        authorityHash.put("actions", actions);
        authorityHash.put("endpoint", "Test");
        listHashes.add(authorityHash);
        userDtoTest = UserDto.builder()
                .username("usuario_prueba")
                .password("123456")
                .authorities(listHashes)
                .build();
    }

    @Test
    void testFindUserByUsername(){
        //given
        userService.save(userTest);
        //when
        User user = userService.findByUsername(userTest.getUsername()).get();
        //then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(userTest.getUsername());
    }

    @Test
    void testGetUsers(){
        //given
        userService.save(userTest);
        userService.save(User.builder()
                .username("user_2")
                .password("123455")
                .build()
        );
        //when
        List<User> users = userService.getUsers();
        //then
        assertThat(users).isNotNull();
        assertThat(users.size()).isGreaterThan(2);
    }

    @Test
    void testSignup(){
        //given
        authorityService.createAuthorityEndpoint("Test");
        //when
        User user = userService.signup(userDtoTest);
        //then
        assertThat(user).isNotNull();
    }
}
