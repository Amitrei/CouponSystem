package com.amitrei.exceptions;

public class AlreadyExistsException extends  Exception {

    public AlreadyExistsException(String message) {
        super(message + " Already exists exception");
    }
}
