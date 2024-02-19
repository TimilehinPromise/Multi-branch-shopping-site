package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.*;
import com.valuemart.shop.domain.service.abstracts.AuthenticationService;
import com.valuemart.shop.domain.service.abstracts.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/auth", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    public AuthController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseModel customerLogin(@Valid @RequestBody CustomerLoginDTO loginForm) {
        return authenticationService.login(loginForm);
    }

    @PostMapping("/signup")
    public ResponseMessage customerSignUp(@Valid @RequestBody UserCreate userCreate) {
        log.info("customer signup ".concat( userCreate.toString()));
        return authenticationService.signUp(userCreate);
    }

    @PostMapping("/sendResetPassword")
    public ResponseMessage sendResetPassword(@RequestBody SendResetPasswordRequest request){
        return  userService.sendResetPassword(request.getEmail());
    }

    @PostMapping("/resetPassword")
    public ResponseMessage resetPassword(@RequestBody NewPassword password){
        return  userService.resetPassword(password);
    }


}
