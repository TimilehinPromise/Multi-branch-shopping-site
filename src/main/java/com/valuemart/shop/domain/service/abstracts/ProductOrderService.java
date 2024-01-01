package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.persistence.entity.User;

public interface ProductOrderService {
    ResponseMessage convertCartToOrder(User user, Long addressId, String message);

    OrderModel getOrder(Long orderId, Long branchId, User user);

    ResponseMessage updateOrderByAdmin(Long orderId, Long branchId, OrderStatus status, User user);
}
