package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return new LoginDetails(userOptional.get());
    }
}
