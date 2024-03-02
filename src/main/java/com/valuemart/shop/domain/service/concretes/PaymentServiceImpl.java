package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.models.AddressModel;
import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.service.abstracts.*;
import com.valuemart.shop.domain.util.PaymentUtils;
import com.valuemart.shop.persistence.entity.Payment;
import com.valuemart.shop.persistence.entity.PaymentStatus;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final ProductOrderService orderService;

    private final PaymentRepository paymentRepository;

    private final PaymentProcessorFactory factory;

    private final UserService userService;

    private final DeliveryService deliveryService;

    @Override
    public ChargeModel createPayment( User user,Long addressId){
        System.out.println("create payment method");
        AddressModel addressModel = userService.getAddressByAddressId(addressId, user.getId());
        BigDecimal deliveryAmount =  deliveryService.getDeliveryPriceByArea(addressModel.getCity());
        System.out.println(addressModel.toString());
        System.out.println(deliveryAmount);

        Payment.PaymentReference reference = new Payment.PaymentReference();
        reference.setUserId(user.getId().toString());
        reference.setReferenceId(PaymentUtils.generateTransRef());

        System.out.println(reference);

        OrderModel model = orderService.getOrder(Long.valueOf(user.getBranchId()),user);
        System.out.println(model);
        orderService.addDeliveryAmountToOrder(deliveryAmount, model.getOrderId());

        final PaymentProcessor paymentProcessor = factory.getProcessor("Flutterwave");

        Payment payment = new Payment();
        payment.setProvider(paymentProcessor.getName());
        payment.setAmount(model.getAmount().add(model.getDeliveryAmount()));
        payment.setPaymentReference(reference);
        payment.setStatus(PaymentStatus.CREATED);
        paymentRepository.save(payment);
        System.out.println(payment);

        ChargeModel chargeModel = paymentProcessor.initiatePayment(model,user,payment);
        System.out.println(chargeModel);
        return chargeModel;
    }

}
