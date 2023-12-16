package com.valuemart.shop.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valuemart.shop.persistence.entity.User;
import lombok.Data;

@Data
public class AddressDTO {
    private Long addressId;
    private String street;
    private String landmark;
    private String city;


}
