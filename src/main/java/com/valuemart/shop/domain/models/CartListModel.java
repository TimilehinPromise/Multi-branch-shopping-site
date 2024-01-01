package com.valuemart.shop.domain.models;

import com.valuemart.shop.persistence.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CartListModel {
    private Long id;
    private ProductModel product;
    private BigDecimal price;
    private int quantity;
}
