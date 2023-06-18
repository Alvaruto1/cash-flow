package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class IncomeExpenseDto implements ApiResponse {
    private String description;
    private String category;
    private String paymentMethod;
    private String type;
    private BigDecimal value;
    private LocalDateTime date;
    private UUID accountId;
}

