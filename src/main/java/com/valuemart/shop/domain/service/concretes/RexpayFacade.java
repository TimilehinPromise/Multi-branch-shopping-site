package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.service.abstracts.PaymentProcessor;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.persistence.entity.Payment;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.PaymentRepository;

public class RexpayFacade extends AbstractPaymentProvider {
    public RexpayFacade(ProductOrderService orderService, PaymentRepository paymentRepository, PaymentProcessorFactory paymentProcessor) {
        super(orderService,paymentRepository, paymentProcessor);
    }

    @Override
    public String getName() {
        return "Rexpay";
    }

    @Override
    public ChargeModel initiatePayment(OrderModel model, User user, Payment payment) {
        return null;
    }


}
