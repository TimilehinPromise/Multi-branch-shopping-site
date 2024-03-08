package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.persistence.entity.User;

public interface PaymentService {

    ChargeModel createPayment(User user);
}
