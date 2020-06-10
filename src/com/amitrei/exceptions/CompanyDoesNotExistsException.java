package com.amitrei.exceptions;

public class CompanyDoesNotExistsException extends Exception{

    public CompanyDoesNotExistsException() {
        super("Company does not exists.");
    }
}
