package com.cash_flow_app.apicashflow.controller;

import com.cash_flow_app.apicashflow.controller.base.ApiController;
import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import com.cash_flow_app.apicashflow.dtos.UserDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.User;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController extends ApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody UserDto userDto) throws Exception {
        User user;
        try{
            user = userService.signup(userDto);
        } catch (Exception e){
            return ApiController.badRequestError(e.getMessage(), "/api/v1/user/signup");
        }

        if(user != null){
            return ApiController.okResponse(UserDto.builder()
                    .username(userDto.getUsername())
                    .authorities(userDto.getAuthorities())
                    .build()
            );
        }
        return ApiController.badRequestError("No there are authorities, no created user", "/api/v1/user/signup");
    }

}
