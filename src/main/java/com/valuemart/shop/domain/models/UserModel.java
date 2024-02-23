package com.valuemart.shop.domain.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

@Data
@Builder
public class UserModel {


    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String royaltyCode;
    private boolean emailVerified;
    private int branchId;
}
