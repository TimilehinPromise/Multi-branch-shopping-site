package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.DiscountResponse;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.persistence.entity.User;

import java.math.BigDecimal;

public interface ProductOrderService {
    ResponseMessage convertCartToOrder(User user, Long addressId, String message,Boolean useWallet);

    DiscountResponse applyDiscount(User user);

    void addDeliveryAmountToOrder(BigDecimal deliveryAmount, Long orderId);

    OrderModel getOrder(Long orderId, Long branchId, User user);


    OrderModel getOrder(Long branchId, User user);

    ResponseMessage updateOrderByAdmin(Long orderId, Long branchId, OrderStatus status, User user, String message);
}
