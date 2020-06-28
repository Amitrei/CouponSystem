package com.amitrei.exceptions.CompanyExceptions;

public class CannotChangeCompanyNameException extends Exception {

    public CannotChangeCompanyNameException() {
        super("Cannot change company name");
    }
}
