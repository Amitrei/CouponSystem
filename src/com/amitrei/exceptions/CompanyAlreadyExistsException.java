package com.amitrei.exceptions;

public class CompanyAlreadyExistsException extends Exception {


    public CompanyAlreadyExistsException() {
        super("Sorry the company is already exists");
    }
}
