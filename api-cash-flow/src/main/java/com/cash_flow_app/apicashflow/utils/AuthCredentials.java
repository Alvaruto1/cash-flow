package com.cash_flow_app.apicashflow.utils;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthCredentials {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
}
