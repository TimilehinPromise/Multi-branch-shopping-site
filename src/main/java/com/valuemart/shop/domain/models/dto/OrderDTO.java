package com.valuemart.shop.domain.models.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long addressId;
    private String message;
    private Boolean useWallet;
}
