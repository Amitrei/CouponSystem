package com.amitrei.exceptions;

public class CustomerAlreadyExistsException extends Exception {
    public CustomerAlreadyExistsException(String email) {
        super("Sorry customer " + email + " is already exists");
    }

    public CustomerAlreadyExistsException() {
        super("Sorry this customer is already exists");
    }
}
