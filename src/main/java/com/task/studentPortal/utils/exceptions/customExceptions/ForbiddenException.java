package com.task.studentPortal.utils.exceptions.customExceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
