package com.valuemart.shop.domain.models.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CartDto {
    private Long id;
    private @NotNull String skuId;
    private @NotNull Integer quantity;
}
