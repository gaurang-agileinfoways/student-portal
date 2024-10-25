package com.task.studentPortal.utils.exceptions.customExceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
