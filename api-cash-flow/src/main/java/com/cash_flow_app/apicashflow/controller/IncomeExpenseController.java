package com.cash_flow_app.apicashflow.controller;

import com.cash_flow_app.apicashflow.controller.base.ApiController;
import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/income_expense")
@RequiredArgsConstructor
public class IncomeExpenseController extends ApiController {

    private final IncomeExpenseService incomeExpenseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody IncomeExpenseDto incomeExpenseDto) throws Exception {
        IncomeExpense incomeExpense;
        try{
            incomeExpense = incomeExpenseService.save(incomeExpenseDto);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/income_expense/create");
        }
        return ApiController.okResponse(incomeExpenseService.incomeExpenseToDto(incomeExpense));
    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<ApiResponse> get(@PathVariable String uuid) throws Exception {
        IncomeExpense incomeExpense;
        try{
            Optional<IncomeExpense> incomeExpenseOptional = incomeExpenseService.getIncomeExpense(UUID.fromString(uuid));
            if (incomeExpenseOptional.isEmpty()) {
                throw new Exception("Income Expense not found");
            }
            incomeExpense = incomeExpenseOptional.get();
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/income_expense/get");
        }
        return ApiController.okResponse(incomeExpenseService.incomeExpenseToDto(incomeExpense));
    }

    @GetMapping("/get_all/expenses/{account_id}")
    public ResponseEntity<ApiResponse> getAllExpensesByAccountId(@PathVariable String account_id) throws Exception {
        List<IncomeExpense> expenses;
        try{
            expenses = incomeExpenseService.getExpensesByAccountId(UUID.fromString(account_id));
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/income_expense/get_all/expenses");
        }
        return ApiController.okResponse(incomeExpenseService.incomesExpensesToDtos(expenses));
    }

    @GetMapping("/get_all/incomes/{account_id}")
    public ResponseEntity<ApiResponse> getAllIncomesByAccountId(@PathVariable String account_id) throws Exception {
        List<IncomeExpense> incomes;
        try{
            incomes = incomeExpenseService.getExpensesByAccountId(UUID.fromString(account_id));
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/income_expense/get_all/incomes");
        }
        return ApiController.okResponse(incomeExpenseService.incomesExpensesToDtos(incomes));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String uuid) throws Exception {
        IncomeExpense incomeExpense;
        try{
            Optional<IncomeExpense> incomeExpenseOptional = incomeExpenseService.getIncomeExpense(UUID.fromString(uuid));
            if (incomeExpenseOptional.isEmpty()) {
                throw new Exception("Income Expense not found");
            }
            incomeExpense = incomeExpenseOptional.get();
            incomeExpenseService.delete(incomeExpense);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/income_expense/delete");
        }
        return ApiController.okResponse(incomeExpenseService.incomeExpenseToDto(incomeExpense));
    }

}
