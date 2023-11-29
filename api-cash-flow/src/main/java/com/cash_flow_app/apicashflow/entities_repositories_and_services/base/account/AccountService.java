package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account;

import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.AccountDtos;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import io.jsonwebtoken.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    @Transactional
    public Account save(@NonNull AccountDto accountDto) {
        List<User> users = new ArrayList<User>();
        Optional<User> currentUserOptional = userService.getCurrentLoggedUser();
        currentUserOptional.ifPresent(users::add);
        users.addAll(getUsersFromAccountDto(accountDto));
        Account account = Account.builder()
                .description(accountDto.getDescription())
                .build();
        users.forEach(account::addUser);
        return accountRepository.save(account);
    }

    @Transactional
    public Account update(@NonNull AccountDto accountDto) {
        List<User> users = new ArrayList<User>();
        Optional<User> currentUserOptional = userService.getCurrentLoggedUser();
        currentUserOptional.ifPresent(users::add);
        users.addAll(getUsersFromAccountDto(accountDto));
        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(accountDto.getId()));
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setDescription(accountDto.getDescription());
            users.forEach((user) -> {
                if(!account.getUsers().contains(user)){
                    account.addUser(user);
                }
            });
            return accountRepository.save(account);
        } else {
            throw new IOException("Account not found");
        }
    }

    private List<User> getUsersFromAccountDto(AccountDto accountDto){
        List<User> users = new ArrayList<User>();
        for (String username : accountDto.getUsers()) {
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isPresent()) {
                users.add(userOptional.get());
            } else {
                throw new IOException("No get user to " + username);
            }
        }
        return users;
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
                .id(account.getId().toString())
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
