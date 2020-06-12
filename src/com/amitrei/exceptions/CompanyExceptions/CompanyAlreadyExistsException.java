package com.amitrei.exceptions.CompanyExceptions;

public class CompanyAlreadyExistsException extends Exception {


    public CompanyAlreadyExistsException() {
        super("Sorry the company is already exists");
    }
}
