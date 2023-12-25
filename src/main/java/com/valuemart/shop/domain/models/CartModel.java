package com.valuemart.shop.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CartModel {

    private List<CartListModel> cartItems;
    private BigDecimal totalCost;
}
