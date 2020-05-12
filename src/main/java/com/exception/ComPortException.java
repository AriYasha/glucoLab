package com.exception;

public class ComPortException extends Exception {

    public ComPortException(String message) {
        super(message);
    }

    public ComPortException(String message, Throwable cause) {
        super(message, cause);
    }
}
