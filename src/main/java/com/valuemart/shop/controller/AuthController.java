package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CustomerLoginDTO;
import com.valuemart.shop.domain.models.LoginResponseModel;
import com.valuemart.shop.domain.models.UserCreate;
import com.valuemart.shop.domain.service.abstracts.AuthenticationService;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/auth", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login/customer")
    public LoginResponseModel customerLogin(@Valid @RequestBody CustomerLoginDTO loginForm) {
        return authenticationService.customerLogin(loginForm);
    }

    @PostMapping("/signup/customer")
    public ResponseMessage customerSignUp(@Valid @RequestBody UserCreate userCreate) {
        log.info("customer signup ".concat( userCreate.toString()));
        return authenticationService.createCustomer(userCreate);
    }


    @PostMapping("/signup/admin")
    public ResponseMessage adminSignUp(@Valid @RequestBody UserCreate userCreate) {
        log.info("admin signup ".concat( userCreate.toString()));
        return authenticationService.createCustomer(userCreate);
    }

    @PostMapping("/login/admin")
    public LoginResponseModel adminLogin(@Valid @RequestBody CustomerLoginDTO loginForm) {
        return authenticationService.customerLogin(loginForm);
    }
}
