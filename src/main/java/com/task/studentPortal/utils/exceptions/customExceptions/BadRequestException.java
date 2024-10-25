package com.task.studentPortal.utils.exceptions.customExceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
