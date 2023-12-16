package com.valuemart.shop.exception;

public class ReferralCodeNotExistException extends IllegalArgumentException{
    public ReferralCodeNotExistException(String msg) {
        super(msg);
    }
}
