package com.laundy.laundrybackend.exception;

public class OrderCannotBeCancelException extends RuntimeException{
    public OrderCannotBeCancelException(String message) {
        super(message);
    }
}
