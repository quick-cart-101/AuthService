package com.quickcart.authservice.exceptions;

public class InvalidUserDetailsException extends Exception {
    public InvalidUserDetailsException(String message) {
        super(message);
    }
}
