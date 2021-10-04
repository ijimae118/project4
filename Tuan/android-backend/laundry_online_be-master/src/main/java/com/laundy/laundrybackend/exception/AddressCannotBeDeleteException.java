package com.laundy.laundrybackend.exception;

public class AddressCannotBeDeleteException extends RuntimeException{
    public AddressCannotBeDeleteException(String message) {
        super(message);
    }
}
