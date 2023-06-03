package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode
@RequiredArgsConstructor
public class LoginDetails implements UserDetails {
    private final User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getAuthorities().forEach(
                authority -> authorities.add(
                        new SimpleGrantedAuthority(
                                    "ENDPOINT_" +
                                        authority.getEndpoint().toUpperCase() +
                                        "_PERMISSION_" +
                                        authority.getPermission().toString().toUpperCase()
                        )
                )
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
