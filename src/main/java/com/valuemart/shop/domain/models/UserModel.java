package com.valuemart.shop.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;

@Data
@Builder
@ToString
public class UserModel {

    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String royaltyCode;
    private boolean emailVerified;
    private int branchId;
    private String royaltyImage;
}
