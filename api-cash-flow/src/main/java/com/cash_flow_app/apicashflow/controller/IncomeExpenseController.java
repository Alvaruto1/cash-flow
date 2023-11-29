package com.cash_flow_app.apicashflow.controller;

import com.cash_flow_app.apicashflow.controller.base.ApiController;
import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import com.cash_flow_app.apicashflow.dtos.IncomeExpenseDto;
import com.cash_flow_app.apicashflow.dtos.ScheduledAccountDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpenseService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.ScheduledAccountService;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @GetMapping("/get_all/expenses/filter/{account_id}")
    public ResponseEntity<ApiResponse> getAllExpensesByAccountIdFilter(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @PathVariable String account_id) throws Exception {
        List<IncomeExpense> expenses;
        System.out.println(startDate);
        try{
            LocalDate parsedStartDate = LocalDate.parse(startDate);
            LocalDate parsedEndDate = LocalDate.parse(endDate);
            expenses = incomeExpenseService.getExpensesByAccountIdAndDate(UUID.fromString(account_id), parsedStartDate, parsedEndDate);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/income_expense/get_all/expenses");
        }
        return ApiController.okResponse(incomeExpenseService.incomesExpensesToDtos(expenses));
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

    @GetMapping("/get_all/incomes/filter/{account_id}")
    public ResponseEntity<ApiResponse> getAllIncomesByAccountIdFilter(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @PathVariable String account_id) throws Exception {
        List<IncomeExpense> incomes;
        try{
            LocalDate parsedStartDate = LocalDate.parse(startDate);
            LocalDate parsedEndDate = LocalDate.parse(endDate);
            incomes = incomeExpenseService.getIncomesByAccountIdAndDate(UUID.fromString(account_id), parsedStartDate, parsedEndDate);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/income_expense/get_all/incomes");
        }
        return ApiController.okResponse(incomeExpenseService.incomesExpensesToDtos(incomes));
    }

    @GetMapping("/get_all/incomes/filter/{account_id}/csv")
    public ResponseEntity<byte[]> getAllIncomesByAccountIdFilterCSV(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @PathVariable String account_id) throws Exception {

        List<IncomeExpense> incomes;
        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parsedEndDate = LocalDate.parse(endDate);
        incomes = incomeExpenseService.getIncomesByAccountIdAndDate(UUID.fromString(account_id), parsedStartDate, parsedEndDate);
        return getResponseEntity(incomes);
    }

    private ResponseEntity<byte[]> getResponseEntity(List<IncomeExpense> incomes_expenses) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "archivo.csv");
        byte[] csvBytes= new byte[0];
        try (StringWriter writer = new StringWriter(); CSVWriter csvWriter = new CSVWriter(writer)) {
            csvWriter.writeNext(new String[]{"id", "description", "date", "value", "category", "payment_method", "has_scheduled_account"});
            for (IncomeExpense income : incomes_expenses) {
                csvWriter.writeNext(new String[]{
                        income.getId().toString(),
                        income.getDescription(),
                        income.getDate().toString(),
                        income.getValue().toString(),
                        income.getCategory().name(),
                        income.getPaymentMethod().name(),
                        String.valueOf(income.getScheduledAccount() != null)
                });
            }
            csvBytes = writer.toString().getBytes();

            return new ResponseEntity<>(csvBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(csvBytes, headers, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get_all/expenses/filter/{account_id}/csv")
    public ResponseEntity<byte[]> getAllExpensesByAccountIdFilterCSV(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @PathVariable String account_id) throws Exception {

        List<IncomeExpense> expenses;
        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parsedEndDate = LocalDate.parse(endDate);
        expenses = incomeExpenseService.getExpensesByAccountIdAndDate(UUID.fromString(account_id), parsedStartDate, parsedEndDate);
        return getResponseEntity(expenses);
    }

    @GetMapping("/get_all/incomes/{account_id}")
    public ResponseEntity<ApiResponse> getAllIncomesByAccountId(@PathVariable String account_id) throws Exception {
        List<IncomeExpense> incomes;
        try{
            incomes = incomeExpenseService.getIncomesByAccountId(UUID.fromString(account_id));
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
