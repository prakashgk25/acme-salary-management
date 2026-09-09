package com.acme.salary.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String m) {
        super(m);
    }
}
