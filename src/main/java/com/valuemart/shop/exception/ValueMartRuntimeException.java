package com.valuemart.shop.exception;

public class ValueMartRuntimeException extends RuntimeException {

    public ValueMartRuntimeException(String message) {
        super(message);
    }

    public ValueMartRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
