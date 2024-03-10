package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.QrCodeResponse;
import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.DiscountResponse;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.persistence.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface ProductOrderService {
    ResponseMessage convertCartToOrder(User user, Long addressId, String message,Boolean useWallet);

    DiscountResponse applyDiscount(User user);

    void addDeliveryAmountToOrder(BigDecimal deliveryAmount, Long orderId);

    OrderModel getOrder(Long orderId, Long branchId, User user);

    List<OrderModel> getAllOrdersByStaff(String status, Long branchId);

    OrderModel getOrder(Long branchId, User user);

    ResponseMessage updateOrderByAdmin(Long orderId, Long branchId, OrderStatus status, User user, String message);

    QrCodeResponse qrCodeResponse(String code);
}
