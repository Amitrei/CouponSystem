package com.amitrei.exceptions;

public class CompanyDoesnotExistsException extends Exception{

    public CompanyDoesnotExistsException() {
        super("Company does not exists.");
    }
}
