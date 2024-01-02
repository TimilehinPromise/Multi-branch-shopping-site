package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CustomerLoginDTO;
import com.valuemart.shop.domain.models.LoginResponseModel;
import com.valuemart.shop.domain.models.UserCreate;
import com.valuemart.shop.persistence.entity.User;

import javax.transaction.Transactional;

public interface AuthenticationService {


    LoginResponseModel login(CustomerLoginDTO loginForm);

    ResponseMessage signUp(UserCreate userCreate);

}
