package com.valuemart.shop.controller;

import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.domain.models.dto.PaymentDTO;
import com.valuemart.shop.domain.service.abstracts.PaymentService;
import com.valuemart.shop.domain.service.concretes.AbstractPaymentProvider;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/api/payment", produces = APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("")
    private ChargeModel initiatePayment(){
        User user = UserUtils.getLoggedInUser();
       return paymentService.createPayment(user);
    }
}
