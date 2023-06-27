package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class AccountDtos implements ApiResponse {
    @Builder.Default
    private ArrayList<AccountDto> accountDtos = new ArrayList<>();
}
