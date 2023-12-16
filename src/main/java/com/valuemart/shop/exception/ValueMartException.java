package com.valuemart.shop.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ValueMartException extends Exception {
    private final HttpStatus httpStatus;

    public ValueMartException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public ValueMartException(String message) {
        super(message);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public ValueMartException(Throwable cause) {
        super(cause);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public ValueMartException(Throwable cause, HttpStatus status) {
        super(cause);
        this.httpStatus = status;
    }

    public ValueMartException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = INTERNAL_SERVER_ERROR;
    }

    public ValueMartException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
