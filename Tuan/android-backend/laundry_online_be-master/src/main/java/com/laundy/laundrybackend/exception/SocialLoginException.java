package com.laundy.laundrybackend.exception;

public class SocialLoginException extends RuntimeException{
    public SocialLoginException(String message) {
        super(message);
    }
}
