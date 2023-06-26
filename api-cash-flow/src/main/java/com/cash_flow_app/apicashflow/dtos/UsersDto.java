package com.cash_flow_app.apicashflow.dtos;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsersDto implements ApiResponse {
    private List<UserDto> userDtos;
}
