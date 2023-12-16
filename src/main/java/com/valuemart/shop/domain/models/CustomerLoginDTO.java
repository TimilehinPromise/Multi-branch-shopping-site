package com.valuemart.shop.domain.models;

import lombok.Data;

import static java.util.Optional.ofNullable;

@Data
public class CustomerLoginDTO {
    private String email;
    private String password;

    public String getEmail() {
        return ofNullable(email)
                .map(String::toLowerCase).orElse(email);
    }
}
