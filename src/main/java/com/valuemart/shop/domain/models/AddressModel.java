package com.valuemart.shop.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressModel {
    private Long addressId;
    private String name;
    private String street;
    private String landmark;
    private String city;
    private boolean preferred;
}
