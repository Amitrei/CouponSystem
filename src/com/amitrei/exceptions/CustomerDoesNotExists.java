package com.amitrei.exceptions;

public class CustomerDoesNotExists extends  Exception {
    public CustomerDoesNotExists() {
        super("Sorry this customer is not exists.");
    }
}
