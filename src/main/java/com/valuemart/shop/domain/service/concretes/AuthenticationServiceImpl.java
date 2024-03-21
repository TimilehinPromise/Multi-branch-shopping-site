package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.*;
import com.valuemart.shop.domain.service.abstracts.AuthenticationService;
import com.valuemart.shop.domain.service.abstracts.EmailService;
import com.valuemart.shop.domain.service.abstracts.QRCodeService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.exception.ValueMartRuntimeException;
import com.valuemart.shop.exception.ValueMartException;
import com.valuemart.shop.persistence.entity.Branch;
import com.valuemart.shop.persistence.entity.Role;
import com.valuemart.shop.persistence.entity.TokenStore;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.BranchRepository;
import com.valuemart.shop.persistence.repository.RoleRepository;
import com.valuemart.shop.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.valuemart.shop.domain.models.RoleType.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static String ERROR_MSG = "Invalid credentials";
    private final static String ACCOUNT_LOCKOUT = "Account has been Locked";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BranchRepository branchRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenAuthenticationService tokenService;

    private final EmailService emailService;

    private final QRCodeService qrCodeService;

    @Override
    public LoginResponseModel customerLogin(CustomerLoginDTO loginForm) {
        User user = getUser(loginForm.getEmail());
        if (user.getRole().getName() != "ROLE_CUSTOMER"){
            throw new BadRequestException("Wrong Credentials");
        }
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
    public LoginResponseModel staffLogin(CustomerLoginDTO loginForm) {
        User user = getUser(loginForm.getEmail());

        if (user.getRole().getName() != "ROLE_STAFF"){
            throw new BadRequestException("Wrong Credentials");
        }
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
    public LoginResponseModel adminLogin(CustomerLoginDTO loginForm) {
        User user = getUser(loginForm.getEmail());

        if (user.getRole().getName() != "ROLE_ADMIN"){
            throw new BadRequestException("Wrong Credentials");
        }

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
    public ResponseMessage signUp(UserCreate userCreate){
            return createCustomer(userCreate);
    }

    @Override
    public ResponseMessage adminSignUp(UserCreate userCreate){
            return createAdmin(userCreate);
    }


    @Override
    public ResponseMessage createStaffByAdmin(UserCreate userCreate, User user){
       return createStaff(userCreate, user);
    }

    @Override
    public ResponseMessage disableStaffByAdmin(Long userId){
       Optional<User> optionalUser = userRepository.findById(userId);

       if (optionalUser.isEmpty()){
           throw new BadRequestException("Staff not found");
       }
       User user = optionalUser.get();
       if (!user.isEnabled()){
           throw new BadRequestException("Staff already disabled");
       }
       user.setEnabled(false);

       userRepository.save(user);

       return  ResponseMessage.builder().message("Staff disabled").build();
    }


    public ResponseMessage createCustomer(UserCreate userCreate){
        try {

            Role role = roleRepository.findFirstByName(RoleType.ROLE_CUSTOMER.name());
            if (!userRepository.existsUserByEmail(userCreate.getEmail())){
          User savedCustomer =   userRepository.save(User.builder().
                    email(userCreate.getEmail()).
                    deleted(false).
                    activated(true).
                    firstName(userCreate.getFirstName()).
                    lastName(userCreate.getLastName()).
                    phone(userCreate.getPhone())
                             .branchId(Integer.parseInt(userCreate.getBranchId()))
                    .role(role)
                    .password(passwordEncoder.encode(userCreate.getPassword()))
                    .enabled(true)
                            .authorities(Collections.emptyList())
                    .build());
                String customerRoyaltyCode = UserUtils.generateCustomerCode(savedCustomer.getFirstName().concat(" ").concat(savedCustomer.getLastName()),savedCustomer.getId(), LocalDateTime.now());
                savedCustomer.setRoyaltyCode(customerRoyaltyCode);
                savedCustomer.setRoyaltyQr(qrCodeService.generateQRCodeImageAndUpload(customerRoyaltyCode));
                userRepository.save(savedCustomer);

                emailService.sendCustomerCreationEmail(savedCustomer);
            log.info("Customer successfully created");
        return ResponseMessage.builder().message("Customer Signed Up Successfully").build();

        }}

        catch (Exception e){
            log.info("error creating a customer"+ e);
        }

        return null;
    }


    public ResponseMessage createAdmin(UserCreate userCreate){
        try {

            Role role = roleRepository.findFirstByName(ROLE_ADMIN.name());
            if (!userRepository.existsUserByEmail(userCreate.getEmail())){
                userRepository.save(User.builder().
                        enabled(true).
                        email(userCreate.getEmail()).
                        firstName(userCreate.getFirstName()).
                        lastName(userCreate.getLastName())
                        .role(role)
                        .password(passwordEncoder.encode(userCreate.getPassword()))
                        .enabled(true)
                        .authorities(Collections.emptyList())
                        .build());
                log.info("Admin successfully created");
                return ResponseMessage.builder().message("Admin Signed Up Successfully").build();

            }}

        catch (Exception e){
            log.info("error creating a admin"+ e);
        }

        return null;
    }


    public ResponseMessage createStaff(UserCreate userCreate,User user){
        try {

            Role role = roleRepository.findFirstByName(ROLE_STAFF.name());
            Branch branch = branchRepository.findById(Long.valueOf(userCreate.getBranchId())).orElseThrow();
            if (!userRepository.existsUserByEmail(userCreate.getEmail())){
              User savedStaff =  userRepository.save(User.builder().
                        email(userCreate.getEmail()).
                        firstName(userCreate.getFirstName()).
                        lastName(userCreate.getLastName())
                        .role(role)
                        .branchId(branch.getId().intValue())
                        .password(passwordEncoder.encode(userCreate.getPassword()))
                        .enabled(true)
                        .authorities(Collections.emptyList())
                        .build());
                log.info("Staff successfully created by Admin " + user.getFirstName());

                emailService.sendStaffCreationEmail(savedStaff,user,branch.getName());
                return ResponseMessage.builder().message("Staff successfully created by Admin"+ user.getFirstName()).build();

            }}

        catch (Exception e){
            log.info("error creating a Staff"+ e);
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

    private void ensureUserIsAdmin(User user)  {
        if (!isAdmin(user)) {
            throw invalidCredentialException();
        }
    }

    public static boolean isAdmin(User user) {
        return ROLE_ADMIN.name().equals(user.getRole().getName());
    }
}
