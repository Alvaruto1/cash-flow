package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeExpenseDto implements ApiResponse {
    private String id;
    private String description;
    private String category;
    private String paymentMethod;
    private String type;
    private BigDecimal value;
    private LocalDateTime date;
    private String accountId;
}

