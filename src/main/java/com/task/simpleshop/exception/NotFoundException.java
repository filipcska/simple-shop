package com.task.simpleshop.exception;


public abstract class NotFoundException extends Exception {

    protected NotFoundException(String message) {
        super(message);
    }

}
