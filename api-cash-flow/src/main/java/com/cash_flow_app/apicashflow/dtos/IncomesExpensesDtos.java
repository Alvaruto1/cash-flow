package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.income_expense.IncomeExpense;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class IncomesExpensesDtos implements ApiResponse {
    @Builder.Default
    private ArrayList<IncomeExpenseDto> incomesExpensesDtos = new ArrayList<>();
}
