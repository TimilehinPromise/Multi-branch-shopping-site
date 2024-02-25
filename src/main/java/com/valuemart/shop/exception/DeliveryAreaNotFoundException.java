package com.valuemart.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeliveryAreaNotFoundException extends RuntimeException {
    public DeliveryAreaNotFoundException(String message) {
        super(message);
    }
}
