package com.task.studentPortal.utils.exceptions.customExceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException() {
        super("Something went wrong, please try again!!");
    }
}
