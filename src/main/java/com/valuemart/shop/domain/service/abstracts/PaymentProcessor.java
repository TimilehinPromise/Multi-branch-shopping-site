package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.persistence.entity.Payment;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.providers.flutterwave.FlwTransactionResponse;

public interface PaymentProcessor {

    String getName();

    ChargeModel initiatePayment(OrderModel model, User user, Payment payment);

    FlwTransactionResponse tsq(String transId);
}
