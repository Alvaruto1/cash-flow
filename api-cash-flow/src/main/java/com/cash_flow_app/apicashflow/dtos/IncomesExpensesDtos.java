package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@Builder
public class IncomesExpensesDtos implements ApiResponse {
    @Builder.Default
    private ArrayList<IncomeExpenseDto> incomesExpensesDtos = new ArrayList<>();
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;
}
