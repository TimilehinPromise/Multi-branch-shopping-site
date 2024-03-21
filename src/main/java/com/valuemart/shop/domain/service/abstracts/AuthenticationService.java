package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CustomerLoginDTO;
import com.valuemart.shop.domain.models.LoginResponseModel;
import com.valuemart.shop.domain.models.UserCreate;
import com.valuemart.shop.persistence.entity.User;

import javax.transaction.Transactional;

public interface AuthenticationService {


    LoginResponseModel customerLogin(CustomerLoginDTO loginForm);

    LoginResponseModel staffLogin(CustomerLoginDTO loginForm);

    LoginResponseModel adminLogin(CustomerLoginDTO loginForm);

    ResponseMessage signUp(UserCreate userCreate);

    ResponseMessage adminSignUp(UserCreate userCreate);

    ResponseMessage createStaffByAdmin(UserCreate userCreate, User user);

    ResponseMessage disableStaffByAdmin(Long userId);
}
