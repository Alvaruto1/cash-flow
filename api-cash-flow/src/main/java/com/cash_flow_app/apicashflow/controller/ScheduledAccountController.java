package com.cash_flow_app.apicashflow.controller;

import com.cash_flow_app.apicashflow.controller.base.ApiController;
import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import com.cash_flow_app.apicashflow.dtos.ScheduledAccountDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.ScheduledAccount;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.ScheduledAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/scheduled_account")
@RequiredArgsConstructor
public class ScheduledAccountController extends ApiController {

    private final ScheduledAccountService scheduledAccountService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody ScheduledAccountDto scheduledAccountDto) throws Exception {
        ScheduledAccount scheduledAccount;
        try{
            scheduledAccount = scheduledAccountService.save(scheduledAccountDto);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/scheduled_account/create");
        }
        return ApiController.okResponse(scheduledAccountService.scheduledAccountToDto(scheduledAccount));
    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<ApiResponse> get(@PathVariable String uuid) throws Exception {
        ScheduledAccount scheduledAccount;
        try{
            Optional<ScheduledAccount> incomeExpenseOptional = scheduledAccountService.getScheduledAccount(UUID.fromString(uuid));
            if (incomeExpenseOptional.isEmpty()) {
                throw new Exception("Scheduled Account not found");
            }
            scheduledAccount = incomeExpenseOptional.get();
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/scheduled_account/get");
        }
        return ApiController.okResponse(scheduledAccountService.scheduledAccountToDto(scheduledAccount));
    }

    @GetMapping("/get/by_income_expense/{income_expense_id}")
    public ResponseEntity<ApiResponse> getScheduledAccountByIncomeExpenseId(@PathVariable String income_expense_id) throws Exception {
        ScheduledAccount scheduledAccount;
        try{
            Optional<ScheduledAccount> incomeExpenseOptional = scheduledAccountService.getScheduledAccountByIncomeExpenseId(UUID.fromString(income_expense_id));
            if (incomeExpenseOptional.isEmpty()) {
                throw new Exception("Income-expense not found");
            }
            scheduledAccount = incomeExpenseOptional.get();
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/scheduled_account/get");
        }
        return ApiController.okResponse(scheduledAccountService.scheduledAccountToDto(scheduledAccount));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String uuid) throws Exception {
        ScheduledAccount scheduledAccount;
        try{
            Optional<ScheduledAccount> scheduledAccountOptional = scheduledAccountService.getScheduledAccount(UUID.fromString(uuid));
            if (scheduledAccountOptional.isEmpty()) {
                throw new Exception("Scheduled Account not found");
            }
            scheduledAccount = scheduledAccountOptional.get();
            scheduledAccountService.delete(scheduledAccount);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/scheduled_account/delete");
        }
        return ApiController.okResponse(scheduledAccountService.scheduledAccountToDto(scheduledAccount));
    }

}
