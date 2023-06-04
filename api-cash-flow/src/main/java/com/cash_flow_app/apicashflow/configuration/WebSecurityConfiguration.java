package com.cash_flow_app.apicashflow.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class WebSecurityConfiguration {

    private UserDetailsService userDetailsService;
    private JWTAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return http
                .csrf().disable()
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
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        /*return http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();*/
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

}
