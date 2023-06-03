package com.cash_flow_app.apicashflow.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_READ")
                .requestMatchers(HttpMethod.POST, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_CREATE")
                .requestMatchers(HttpMethod.PUT, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_UPDATE")
                .requestMatchers(HttpMethod.PATCH, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_DELETE")

                .requestMatchers(HttpMethod.GET, "/demo").hasAuthority("ENDPOINT_DEMO_PERMISSION_READ")
                .requestMatchers(HttpMethod.POST, "/demo").hasAuthority("ENDPOINT_DEMO_PERMISSION_CREATE")
                .requestMatchers(HttpMethod.PUT, "/demo").hasAuthority("ENDPOINT_DEMO_PERMISSION_UPDATE")
                .requestMatchers(HttpMethod.PATCH, "/demo").hasAuthority("ENDPOINT_DEMO_PERMISSION_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/demo").hasAuthority("ENDPOINT_DEMO_PERMISSION_DELETE")
                .and()
                .csrf()
                .disable()
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
