package com.amitrei.exceptions.CompanyExceptions;

public class CompanyDoesNotExistsException extends Exception{

    public CompanyDoesNotExistsException() {
        super("Company does not exists.");
    }
}
