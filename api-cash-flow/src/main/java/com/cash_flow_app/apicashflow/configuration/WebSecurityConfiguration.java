package com.cash_flow_app.apicashflow.configuration;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security.JWTAuthenticationFilter;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security.JWTAuthorizationFilter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class WebSecurityConfiguration {

    private UserDetailsService userDetailsService;
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/user/login");

        return http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/v1/account/create").hasAuthority("ENDPOINT_ACCOUNT_PERMISSION_CREATE")
                .requestMatchers(HttpMethod.PUT, "/api/v1/account/update").hasAuthority("ENDPOINT_ACCOUNT_PERMISSION_CREATE")
                .requestMatchers(HttpMethod.GET, "/api/v1/account/get/{uuid}").hasAuthority("ENDPOINT_ACCOUNT_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/account/get_all/by_username/{username}").hasAuthority("ENDPOINT_ACCOUNT_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/account/get_all/by_current_user").hasAuthority("ENDPOINT_ACCOUNT_PERMISSION_READ")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/account/delete/{uuid}").hasAuthority("ENDPOINT_ACCOUNT_PERMISSION_DELETE")

                .requestMatchers(HttpMethod.POST, "/api/v1/income_expense/create").hasAuthority("ENDPOINT_INCOMEEXPENSE_PERMISSION_CREATE")
                .requestMatchers(HttpMethod.GET, "/api/v1/income_expense/get/{uuid}").hasAuthority("ENDPOINT_INCOMEEXPENSE_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/income_expense/get_all/expenses/{account_id}").hasAuthority("ENDPOINT_INCOMEEXPENSE_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/income_expense/get_all/expenses/filter/**").hasAuthority("ENDPOINT_INCOMEEXPENSE_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/income_expense/get_all/incomes/{account_id}").hasAuthority("ENDPOINT_INCOMEEXPENSE_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/income_expense/get_all/incomes/filter/**").hasAuthority("ENDPOINT_INCOMEEXPENSE_PERMISSION_READ")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/income_expense/delete/{uuid}").hasAuthority("ENDPOINT_INCOMEEXPENSE_PERMISSION_DELETE")

                .requestMatchers(HttpMethod.POST, "/api/v1/scheduled_account/create").hasAuthority("ENDPOINT_SCHEDULEDACCOUNT_PERMISSION_CREATE")
                .requestMatchers(HttpMethod.GET, "/api/v1/scheduled_account/get/{uuid}").hasAuthority("ENDPOINT_SCHEDULEDACCOUNT_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/scheduled_account/get/by_income_expense/{income_expense_id}").hasAuthority("ENDPOINT_SCHEDULEDACCOUNT_PERMISSION_READ")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/scheduled_account/delete/{uuid}").hasAuthority("ENDPOINT_SCHEDULEDACCOUNT_PERMISSION_DELETE")

                .requestMatchers(HttpMethod.GET, "/api/v1/user/get_all").hasAuthority("ENDPOINT_USER_PERMISSION_READ")
                .requestMatchers(HttpMethod.GET, "/api/v1/user/get/{username}").hasAuthority("ENDPOINT_USER_PERMISSION_READ")

                .requestMatchers(HttpMethod.POST, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_CREATE")
                .requestMatchers(HttpMethod.PUT, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_UPDATE")
                .requestMatchers(HttpMethod.PATCH, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/home").hasAuthority("ENDPOINT_HOME_PERMISSION_DELETE")

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
                .requestMatchers(HttpMethod.POST, "/api/v1/user/signup").permitAll()
                .requestMatchers("/health").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://192.168.1.8:19006");
        config.addAllowedOriginPattern("http://192.168.1.8:19006");
        config.addAllowedHeader("*");

        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}
