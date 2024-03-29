package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@Builder
public class UserResponseDto implements ApiResponse {
    private String username;
    private String password;
    private String token_auth;
    private ArrayList<HashMap<String, Object>> authorities;
}

