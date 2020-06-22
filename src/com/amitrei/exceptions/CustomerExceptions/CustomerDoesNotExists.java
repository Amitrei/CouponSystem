package com.amitrei.exceptions.CustomerExceptions;

public class CustomerDoesNotExists extends Exception {

    public CustomerDoesNotExists() {
        super("Sorry this customer is not exists.");
    }


}
