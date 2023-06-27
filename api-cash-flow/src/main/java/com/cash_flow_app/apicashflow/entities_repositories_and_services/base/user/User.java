package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user;

import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.Authority;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="users")
public class User {

    public User(String username, String password, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique=true)
    private String username;

    private String password;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    private List<Authority> authorities;

    @ManyToMany(fetch=FetchType.EAGER)
    @Builder.Default
    @JoinTable(name = "user_account",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id")
    )
    private List<Account> accounts = new ArrayList<>();

}
