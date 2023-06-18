package com.cash_flow_app.apicashflow.controller;

import com.cash_flow_app.apicashflow.controller.base.ApiController;
import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.AccountDtos;
import com.cash_flow_app.apicashflow.dtos.UserDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.Account;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.account.AccountService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController extends ApiController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody AccountDto accountDto) throws Exception {
        Account account;
        try{
            account = accountService.save(accountDto);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/account/create");
        }
        return ApiController.okResponse(accountService.accountToDto(account));
    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<ApiResponse> get(@PathVariable String uuid) throws Exception {
        Account account;
        try{
            Optional<Account> accountOptional = accountService.getAccount(UUID.fromString(uuid));
            if (accountOptional.isEmpty()) {
                throw new Exception("Account not found");
            }
            account = accountOptional.get();
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/account/get");
        }
        return ApiController.okResponse(accountService.accountToDto(account));
    }

    @GetMapping("/get_all/by_username")
    public ResponseEntity<ApiResponse> getAllByUsername(@PathVariable String username) throws Exception {
        List<Account> accounts = new ArrayList<>();
        try{
            accounts = accountService.getAccountsByUsername(username);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/account/get");
        }
        return ApiController.okResponse(accountService.accountsToDtos(accounts));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String uuid) throws Exception {
        Account account;
        try{Optional<Account> accountOptional = accountService.getAccount(UUID.fromString(uuid));
            if (accountOptional.isEmpty()) {
                throw new Exception("Account not found");
            }
            account = accountOptional.get();
            accountService.delete(account);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/account/delete");
        }
        return ApiController.okResponse(accountService.accountToDto(account));
    }

}
