package com.servlet.exception;

public class FileConstraintException extends ApiException {

    public FileConstraintException(String s) {
        super(s);
        this.message = s;
    }
}
