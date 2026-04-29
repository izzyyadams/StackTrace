package com.stacktrace.exception;

public class AIException extends Exception {
    public AIException(String message) {
        super(message);
    }
    public AIException(String message, Throwable cause) {
        super(message, cause);
    }
}
