package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.domain.service.abstracts.*;
import com.valuemart.shop.domain.util.PaymentUtils;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.persistence.entity.Payment;
import com.valuemart.shop.persistence.entity.PaymentStatus;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final ProductOrderService orderService;

    private final PaymentRepository paymentRepository;

    private final PaymentProcessorFactory factory;


    @Override
    public ChargeModel createPayment(User user){
        System.out.println("create payment method");

        Payment.PaymentReference reference = new Payment.PaymentReference();
        reference.setUserId(user.getId().toString());
        reference.setReferenceId(PaymentUtils.generateTransRef());


        OrderModel model = orderService.getOrder(Long.valueOf(user.getBranchId()),user);

        if (model.getStatus().equals(OrderStatus.IN_PROGRESS) || model.getStatus().equals(OrderStatus.IN_PROGRESS_BUT_DELAYED)){
        System.out.println(model);

        final PaymentProcessor paymentProcessor = factory.getProcessor("Flutterwave");

        Payment payment = new Payment();
        payment.setProvider(paymentProcessor.getName());
        payment.setAmount(model.getAmount());
        payment.setPaymentReference(reference);
        payment.setStatus(PaymentStatus.CREATED);
        paymentRepository.save(payment);
        System.out.println(payment);

        ChargeModel chargeModel = paymentProcessor.initiatePayment(model,user,payment);
        System.out.println(chargeModel);
        return chargeModel;
        }
        else {
            throw new BadRequestException("Order not in right status");
        }
    }

}
