package com.cash_flow_app.apicashflow.utils;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.Authority;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.AuthorityService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Runner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityService authorityService;

    public Runner(UserRepository userRepository, AuthorityService authorityService) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
    }

    @Override
    public void run(String... args) throws Exception  {
        if(this.authorityService.count() == 0){
          this.authorityService.createAuthorityEndpoint("Home");
          this.authorityService.createAuthorityEndpoint("Demo");
        }
        List<Authority> authoritiesDemoByUser2 = authorityService.getAllPermissionsToEndpoint("Demo");
        authoritiesDemoByUser2.add(this.authorityService.findByPermissionAndEndpoint(PermissionName.READ, "Home").get());
        if(this.userRepository.count() == 0){
            this.userRepository.saveAll(List.of(
                    new User("alvaruto", new BCryptPasswordEncoder().encode("123456"), authorityService.getAllPermissionsToEndpoint("Home")),
                    new User("usuario2", new BCryptPasswordEncoder().encode("us2_123456"), authoritiesDemoByUser2)
            ));
        }
    }
}
