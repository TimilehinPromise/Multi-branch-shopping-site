package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CustomerLoginDTO;
import com.valuemart.shop.domain.models.LoginResponseModel;
import com.valuemart.shop.domain.models.RoleType;
import com.valuemart.shop.domain.models.UserCreate;
import com.valuemart.shop.domain.service.abstracts.AuthenticationService;
import com.valuemart.shop.exception.ValueMartRuntimeException;
import com.valuemart.shop.exception.ValueMartException;
import com.valuemart.shop.persistence.entity.Role;
import com.valuemart.shop.persistence.entity.TokenStore;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.RoleRepository;
import com.valuemart.shop.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static String ERROR_MSG = "Invalid credentials";
    private final static String ACCOUNT_LOCKOUT = "Account has been Locked";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenAuthenticationService tokenService;

    @Override
    public LoginResponseModel customerLogin(CustomerLoginDTO loginDTO) {
        try {
            System.out.println(loginDTO);
            return loginCustomer(loginDTO);
        } catch (Exception e) {
//            throw new ValuePlusException(e.getMessage(), UNAUTHORIZED);
        }
        return null;
    }

    private LoginResponseModel loginCustomer(CustomerLoginDTO loginForm) {
        User user = getUser(loginForm.getEmail());
        log.info(user.toString());
        log.info("user fetched");
        checkRetries(user);

        matchPassword(loginForm.getPassword(), user.getPassword(),user);
        user.setRetries(0);
        userRepository.save(user);
       // auditEvent.publish(user, user, USER_LOGIN, AGENT);
        TokenStore store = tokenService.generatorToken(user);
        return  LoginResponseModel.builder()
                .token(store.getToken())
                .expires(store.getExpiredAt())
                .type(user.getRole().getName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public ResponseMessage createCustomer(UserCreate userCreate){
        try {

            Role role = roleRepository.findFirstByName(RoleType.CUSTOMER.name());
            log.info(role.toString());
            if (!userRepository.existsUserByEmail(userCreate.getEmail())){
             userRepository.save(User.builder().
                    enabled(false).
                    email(userCreate.getEmail()).
                    firstName(userCreate.getFirstname()).
                    lastName(userCreate.getLastname())
                    .role(role)
                    .password(passwordEncoder.encode(userCreate.getPassword()))
                    .enabled(true)
                            .authorities(Collections.emptyList())
                    .build());
            log.info("User successfully created");
        return ResponseMessage.builder().message("Customer Signed Up Successfully").build();

        }}

        catch (Exception e){

        }

        return null;
    }

    private User getUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndDeletedFalse(email);
               if (optionalUser.isEmpty()){
                   throw invalidCredentialException();
               }
        User user = optionalUser.get();
        if (!user.isEnabled()) {
            log.warn("User is currently disabled");
            throw invalidCredentialException();
        }

        return user;
    }

    private ValueMartRuntimeException invalidCredentialException() {
        return new ValueMartRuntimeException(ERROR_MSG);
    }

    private void checkRetries(User user) {
        try {
            if (user.getRetries()>=5){
                if (user.isEnabled())
                {
                    user.setEnabled(false);
                    userRepository.save(user);
                }
                throw accountLockedException();
            }}
        catch (ValueMartException e){
            log.info(e.getMessage());
//            throw  ValueMartException(e.getMessage(), UNAUTHORIZED);
        }
    }

    private ValueMartException accountLockedException(){
        return new ValueMartException(ACCOUNT_LOCKOUT);
    }

    private void matchPassword(String plainPassword, String encryptedPassword,User user) {
        if (!passwordEncoder.matches(plainPassword, encryptedPassword)) {
            user.setRetries(user.getRetries()+1);
            userRepository.save(user);
            System.out.println(user);
            throw invalidCredentialException();
        }
    }
}
