package com.valuemart.shop.domain.models.dto;

import com.valuemart.shop.domain.models.enums.OrderStatus;
import lombok.Data;

@Data
public class AdminOrderResponseDTO {
    Long orderId;
    Long branchId;
    OrderStatus status;
    String message;
    Long userId;
}
