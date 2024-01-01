package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.service.abstracts.PaymentProcessor;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.domain.util.PaymentUtils;
import com.valuemart.shop.persistence.entity.Payment;
import com.valuemart.shop.persistence.entity.PaymentStatus;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPaymentProvider implements PaymentProcessor {

    private final ProductOrderService orderService;

    private final PaymentRepository paymentRepository;

    private final PaymentProcessorFactory factory;

//    public ChargeModel createPayment(Long orderId, User user){
//
//        Payment.PaymentReference reference = new Payment.PaymentReference();
//        reference.setUserId(user.getId().toString());
//        reference.setReferenceId(PaymentUtils.generateTransRef());
//
//        OrderModel model = orderService.getOrder(orderId, Long.valueOf(user.getBranchId()),user);
//
//        final PaymentProcessor paymentProcessor = factory.getProcessor("Flutterwave");
//
//        Payment payment = new Payment();
//        payment.setProvider(paymentProcessor.getName());
//        payment.setAmount(model.getAmount());
//        payment.setPaymentReference(reference);
//        payment.setStatus(PaymentStatus.CREATED);
//        paymentRepository.save(payment);
//
//        ChargeModel chargeModel = paymentProcessor.initiatePayment(model,user);
//
//        return chargeModel;
//    }

    public void doTsq(){

    }
}
