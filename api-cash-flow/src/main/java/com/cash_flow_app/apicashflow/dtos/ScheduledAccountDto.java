package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ScheduledAccountDto implements ApiResponse {
    private String periodicity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID incomeExpenseId;
}

