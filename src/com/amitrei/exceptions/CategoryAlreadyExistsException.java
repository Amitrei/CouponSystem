package com.amitrei.exceptions;

public class CategoryAlreadyExistsException extends  Exception {

    public CategoryAlreadyExistsException(String category) {
        super("Category: "+category+" already exists.");
    }
}
