package com.valuemart.shop.domain;

import com.valuemart.shop.domain.models.UserModel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
@Builder
public class QrCodeResponse {

    private BigDecimal amount;

    private UserModel model;
}
