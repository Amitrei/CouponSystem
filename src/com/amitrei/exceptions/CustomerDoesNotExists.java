package com.amitrei.exceptions;

public class CustomerDoesNotExists extends Exception {
    public CustomerDoesNotExists() {
        super("Sorry this customer is not exists.");
    }

    public CustomerDoesNotExists(int CustomerID) {
        super("Sorry the customer id : " + CustomerID + " is not exists");
    }
}
