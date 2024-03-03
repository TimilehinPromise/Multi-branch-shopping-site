package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CustomerLoginDTO;
import com.valuemart.shop.domain.models.LoginResponseModel;
import com.valuemart.shop.domain.models.UserCreate;
import com.valuemart.shop.domain.models.dto.DisableStaffDTO;
import com.valuemart.shop.domain.service.abstracts.AuthenticationService;
import com.valuemart.shop.domain.util.UserUtils;
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
@RequestMapping(value = "v1/api/super", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class SuperController {

    private final AuthenticationService authenticationService;

    public SuperController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseMessage customerSignUp(@Valid @RequestBody UserCreate userCreate) {
        log.info("customer signup ".concat( userCreate.toString()));
        return authenticationService.signUp(userCreate);
    }

    @PostMapping("/createStaff")
    public ResponseMessage createStaff(@Valid @RequestBody UserCreate userCreate) {
        User user = UserUtils.getLoggedInUser();
        return authenticationService.createStaffByAdmin(userCreate,user);
    }

    @PostMapping("/disable/staff")
    public ResponseMessage disableStaff (@RequestBody DisableStaffDTO disableStaffDTO){
       return authenticationService.disableStaffByAdmin(disableStaffDTO.getStaffId());
    }

}
