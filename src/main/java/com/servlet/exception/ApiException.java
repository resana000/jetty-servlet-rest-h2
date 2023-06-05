package com.servlet.exception;

public abstract class ApiException extends RuntimeException {

    String message;

    public ApiException(String s) {
        super(s);
        this.message = s;
    }

}
