package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account;

import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.AccountDtos;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import io.jsonwebtoken.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    public Account save(@NonNull AccountDto accountDto) {
        List<User> users = new ArrayList<User>();
        for (String username : accountDto.getUsers()) {
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isPresent()) {
                users.add(userOptional.get());
            } else {
                throw new IOException("No get user to " + username);
            }
        }
        Account account = Account.builder()
                .description(accountDto.getDescription())
                .build();
        users.forEach(account::addUser);
        return accountRepository.save(account);
    }

    public void delete(@NonNull Account account) {
        accountRepository.delete(account);
    }

    public Optional<Account> getAccount(@NonNull UUID uuid) {
        return accountRepository.findAccountById(uuid);
    }

    public List<Account> getAccountsByUsername(@NonNull String username) {
        return accountRepository.findByUsers_Username(username);
    }

    public AccountDto accountToDto(@NonNull Account account) {
        ArrayList<String> usernames = new ArrayList<>();
        for (User user : account.getUsers()) {
            usernames.add(user.getUsername());
        }
        return AccountDto.builder()
                .description(account.getDescription())
                .users(usernames)
                .build();
    }

    public AccountDtos accountsToDtos(@NonNull List<Account> accounts) {
        AccountDtos accountDtos = AccountDtos.builder().build();
        for (Account account : accounts) {
            accountDtos.getAccountDtos().add(accountToDto(account));
        }
        return accountDtos;
    }

}
