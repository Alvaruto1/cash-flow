package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security;

import com.cash_flow_app.apicashflow.utils.AuthCredentials;
import com.cash_flow_app.apicashflow.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthCredentials authCredentials = new AuthCredentials();
        try{
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
        } catch (IOException e) {
        }
        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
                authCredentials.getUsername(),
                authCredentials.getPassword(),
                authCredentials.getAuthorities()
        );
        return getAuthenticationManager().authenticate(usernamePAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String token = TokenUtils.createToken(userDetails.getUsername());

        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().flush();
        //super.successfulAuthentication(request, response, chain, authResult);
    }
}
